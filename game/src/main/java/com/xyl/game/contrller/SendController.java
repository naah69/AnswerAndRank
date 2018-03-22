package com.xyl.game.contrller;

import com.xyl.game.dto.AnswerDTO;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.*;
import com.xyl.game.utils.*;
import com.xyl.game.websocket.AnswerWebSocket;
import com.xyl.game.websocket.ManageWebSocket;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * sendAnswer
 *
 * @author Naah
 * @date 2018-01-23
 */

@RequestMapping("/admin")
@CrossOrigin
@RestController
public class SendController {

    private static final Logger logger = Logger.getLogger(SendController.class);

    @PostMapping("/sendAnswer")
    public GridPage send() {
        //后台数据
        GridPage result = new GridPage();

        //开始时间或现在题目为空则返回没有一模
        if (HeapVariable.beginTime == null || HeapVariable.now == null) {
            logger.info("未找到题目！");
            result.setErrorCode(FinalVariable.NO_QUESTION_STATUS_CODE);
            result.setMessage(FinalVariable.NO_QUESTION_MESSAGE);
            return result;
        }

        //判断是否已经发送答案。如果已经发送则返回还没发送问题
        if (HeapVariable.isSendAnswer.get() == true) {
            logger.info("已经发送答案，请发送下一题！");
            result.setErrorCode(FinalVariable.NO_SEND_QUESTION_ERROR_STATUS_CODE);
            result.setMessage(FinalVariable.NO_SEND_QUESTION_ERROR_MESSAGE);
            return result;
        }

        //发送答案
        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
        Page<AnswerDTO> answerCount = AnswerUtils.getAnswerCountNow();
        result.setPageList(answerCount);
        try {
            /* 前台数据 */
            GridPage answer = new GridPage();
            answer.setMethod(FinalVariable.ANSWER_METHOD);
            answer.setPageList(answerCount);
            sendAnswerMessage(answer);
            ManageWebSocket.sendRank();
            logger.info("向后台发送排行榜成功！");
            HeapVariable.isSendAnswer.set(true);
            logger.info("发送答案成功！");

        } catch (Exception e) {
            result.setErrorCode(FinalVariable.SEND_ANSWER_ERROR_STATUS_CODE);
            logger.info("发送答案发生异常",e);

        }
        return result;
    }

    @PostMapping("/sendQuestion")
    public GridPage sendQuestion() {
        GridPage result = new GridPage();
        try {
            if (HeapVariable.beginTime == null) {
                logger.info("没有游戏场次！");
                result.setErrorCode(FinalVariable.NO_GAME_STATUS_CODE);
                result.setMessage(FinalVariable.NO_GAME_MESSAGE);
                return result;
            }
            if (HeapVariable.isSendAnswer.get() == false) {
                logger.info("还没发送上一题答案！");
                result.setErrorCode(FinalVariable.NO_SEND_ANSWER_ERROR_STATUS_CODE);
                result.setMessage(FinalVariable.NO_SEND_ANSWER_ERROR_MESSAGE);
                return result;
            }

            result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
            GridPage question = new GridPage();
            question.setMethod(FinalVariable.QUESTION_METHOD);
            QuestionDTO questionDTO = QuestionUtils.getNextQuestionDTO();
            if (questionDTO == null) {
                logger.info("游戏结束！");
                result.setErrorCode(FinalVariable.GAME_DONE_STATUS_CODE);
                result.setMessage(FinalVariable.GAME_DONE_MESSAGE);
                question.setErrorCode(FinalVariable.GAME_DONE_STATUS_CODE);
                question.setMessage(FinalVariable.GAME_DONE_MESSAGE);
            } else {
                Page<QuestionDTO> page = new Page<>();
                Page<AnnualMeetingGameQuestion> manage = new Page<>();
                manage.add(QuestionUtils.getQuestion(questionDTO.getId()));
                page.add(questionDTO);
                question.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
                question.setPageList(page);
                result.setPageList(manage);
            }
            int count = AnswerWebSocket.sendGridPageToAll(question);
            logger.info("本次发送题目" + count + "次");
            HeapVariable.isSendAnswer.set(false);
            logger.info("发送题目成功！");


        } catch (Exception e) {
            result.setErrorCode(FinalVariable.SEND_QUESTION_ERROR_STATUS_CODE);
            logger.info("发送问题发生异常",e);
        }

        return result;
    }


    private void sendAnswerMessage(GridPage answer) {
        int id = HeapVariable.now.getId();
        int rightAnswer = AnswerUtils.getAnswerNow();

        ConcurrentLinkedDeque<AnswerWebSocket> sessionList = AnswerWebSocket.webSocketList;
        int i = 0;

        /*遍历session*/
        for (AnswerWebSocket session : sessionList) {
            User user = UserUtils.getUser(session);
            /*判断用户是否死亡*/
            if (user != null && user.getDieIndex()==-1) {
                /*用户答案列表*/
                List<Answer> answersList = user.getAnswers();

                Answer userAnswer = null;
                Integer times = -1;
                /*是否包含当前答案*/
                if (answersList.size() > id - 1) {
                    userAnswer = answersList.get(id - 1);
                    times = userAnswer.getTime();
                }
                /*答题是否未超时*/
                boolean overTime = times > 0 && times <= HeapVariable.intervalSecond+5;

                /*判断答案是否正确*/
                if (overTime && userAnswer != null && userAnswer.getAnswer() == rightAnswer) {

                    userAnswer.setIsRight(true);
                    user.setScore(user.getScore() + 1);
                    /*如果当前题目为最后一题*/
                    if (id == HeapVariable.questionsList.size()) {
                        answer.setErrorCode(FinalVariable.YOU_WIN_STATUS_CODE);
                        answer.setMessage(FinalVariable.YOU_WIN_MESSAGE);

                    } else {

                        answer.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
                        answer.setMessage(FinalVariable.NORMAL_MESSAGE);
                    }

                    logger.info(user.getUsername() + " 第" + id + "题答案正确！");
                    /*超时*/
                } else if (!overTime) {
                    user.setDieIndex(id);
                    answer.setErrorCode(FinalVariable.TIME_OVER_STATUS_CODE);
                    answer.setMessage(FinalVariable.TIME_OVER_MESSAGE + " " + HeapVariable.intervalSecond + " 秒");
                    logger.info(user.getUsername() + " 第" + id + "题超时！");

                    /*答题错误*/
                } else {
                    user.setDieIndex(id);
                    answer.setErrorCode(FinalVariable.LOST_STATUS_CODE);
                    answer.setMessage(FinalVariable.LOST_MESSAGE);
                    logger.info(user.getUsername() + " 第" + id + "题答案错误！");
                }
                /*死亡用户不判断答案*/
            } else {
                answer.setErrorCode(FinalVariable.GAME_LOST_STATUS_CODE);
                answer.setMessage(FinalVariable.GAME_LOST_MESSAGE);
            }
            /*发送答案*/
            session.sendGridPage(answer);
            i++;
        }
        logger.info("本次发送题目" + i + "次");
    }


}
