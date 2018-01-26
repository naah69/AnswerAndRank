package com.xyl.game.contrller;

import com.xyl.game.dto.AnswerDTO;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.*;
import com.xyl.game.utils.*;
import com.xyl.game.websocket.AnswerWebSocket;
import com.xyl.game.websocket.ManageWebSocket;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/sendAnswer")
    public GridPage send() {
        GridPage result = new GridPage();
        if (HeapVariable.beginTime == null || HeapVariable.now == null) {
            result.setErrorCode(FinalVariable.NO_QUESTION_STATUS_CODE);
            result.setMessage(FinalVariable.NO_QUESTION_MESSAGE);
            return result;
        }
         if (HeapVariable.isSendAnswer.get() == true) {
                result.setErrorCode(FinalVariable.NO_SEND_QUESTION_ERROR_STATUS_CODE);
                result.setMessage(FinalVariable.NO_SEND_QUESTION_ERROR_MESSAGE);
                return result;
            }


        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
        Page<AnswerDTO> answerCount = AnswerUtils.getAnswerCount();
        result.setPageList(answerCount);
        try {
            GridPage answer = new GridPage();
            answer.setMethod(FinalVariable.ANSWER_METHOD);
            answer.setPageList(answerCount);
            sendAnswerMessage(answer);
            ManageWebSocket.sendRank();
            HeapVariable.isSendAnswer.set(true);
        } catch (Exception e) {
            result.setErrorCode(FinalVariable.SEND_ANSWER_ERROR_STATUS_CODE);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/sendQuestion")
    public GridPage sendQuestion() {
        GridPage result = new GridPage();
        try {
            if (HeapVariable.beginTime == null) {
                result.setErrorCode(FinalVariable.NO_GAME_STATUS_CODE);
                result.setMessage(FinalVariable.NO_GAME_MESSAGE);
                return result;
            }
            if (HeapVariable.isSendAnswer.get() == false) {
                result.setErrorCode(FinalVariable.NO_SEND_ANSWER_ERROR_STATUS_CODE);
                result.setMessage(FinalVariable.NO_SEND_ANSWER_ERROR_MESSAGE);
                return result;
            }

            result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
            GridPage question = new GridPage();
            question.setMethod(FinalVariable.QUESTION_METHOD);
            QuestionDTO questionDTO = QuestionUtils.getNextQuestionDTO();
            if (questionDTO == null) {
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

            AnswerWebSocket.sendGridPageToAll(question);
            HeapVariable.isSendAnswer.set(false);

        } catch (Exception e) {
            result.setErrorCode(FinalVariable.SEND_QUESTION_ERROR_STATUS_CODE);
        }

        return result;
    }


    private void sendAnswerMessage(GridPage answer) {
        int id = HeapVariable.now.getId();
        int rightAnswer = AnswerUtils.getAnswerNow();

        ConcurrentLinkedDeque<AnswerWebSocket> sessionList = AnswerWebSocket.webSocketList;
        for (AnswerWebSocket session : sessionList) {
            User user = UserUtils.getUser(session);

            if (user!=null&&user.getDieIndex() == null) {

                Answer userAnswer = user.getAnswers().get(id - 1);
                Integer times = userAnswer.getTime();
                boolean overTime = times > 0 && times <= HeapVariable.intervalSecond;
                if (overTime && userAnswer != null && userAnswer.getAnswer() == rightAnswer) {

                    userAnswer.setIsRight(true);
                    user.setScore(user.getScore() + 1);
                    if (id == HeapVariable.questionsList.size()) {
                        answer.setErrorCode(FinalVariable.YOU_WIN_STATUS_CODE);
                        answer.setMessage(FinalVariable.YOU_WIN_MESSAGE);

                    } else {

                        answer.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
                        answer.setMessage(FinalVariable.NORMAL_MESSAGE);

                    }

                } else if (!overTime) {
                    user.setDieIndex(id);
                    answer.setErrorCode(FinalVariable.TIME_OVER_STATUS_CODE);
                    answer.setMessage(FinalVariable.TIME_OVER_MESSAGE + " " + HeapVariable.intervalSecond + " 秒");
                } else {
                    user.setDieIndex(id);
                    answer.setErrorCode(FinalVariable.LOST_STATUS_CODE);
                    answer.setMessage(FinalVariable.LOST_MESSAGE);
                }
            }else{
                  answer.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
                  answer.setMessage(FinalVariable.NORMAL_MESSAGE);
            }

            session.sendGridPage(answer);

        }
    }


}
