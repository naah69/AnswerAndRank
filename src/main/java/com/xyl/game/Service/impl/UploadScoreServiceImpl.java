package com.xyl.game.Service.impl;

import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.Answer;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.QuestionUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * uploadSocreServiceImpl
 *
 * @author Naah
 * @date 2018-01-21
 */
@Service
public class UploadScoreServiceImpl implements UploadScoreService {
    @Override
    public GridPage<QuestionDTO> uploadScore(Integer id,Byte answer, Integer times, String sessionId, User user) {
        GridPage<QuestionDTO> grid = new GridPage<>();
        grid.setMethod("updateScore");
        Long time=HeapVariable.beginTime.getTime();
        if (HeapVariable.beginTime==null||time>System.currentTimeMillis()) {
            GridPage result = new GridPage();
            result.setErrorCode("1");
            result.setMessage(time+"");
            return result;
        }
        List<Answer> answers = user.getAnswers();
        if (times == null) {
            grid.setErrorCode("21");
            grid.setMessage("time not found");
        }

        if (sessionId == null) {
            grid.setMessage("22");
            grid.setMessage("session not found");
        }

        if (user == null) {
            grid.setErrorCode("23");
            grid.setMessage("user not found");
        }

        if (id !=HeapVariable.now.getId()-1) {
            grid.setErrorCode("24");
            grid.setMessage("id error");
        }

        if (user.getDieIndex() != null) {
            grid.setErrorCode("25");
            grid.setMessage("you had died");
        }

        if (grid.getErrorCode()!=null) {
            return grid;
        }


        if (!id.equals( HeapVariable.now.getId())) {
            grid.setErrorCode("28");
            grid.setMessage("different question");
            return grid;
        }
        answer=answer!=null?answer:0;
        AnnualMeetingGameQuestion question=QuestionUtils.getQuestion(id);
        Answer userAnswer = new Answer(id, question, answer, times,false,new Timestamp(System.currentTimeMillis()));
        answers.add(userAnswer);
        boolean overTime=times>0&&times<=HeapVariable.intervalSecond;
        user.setTimesSecond(user.getTimesSecond() + times);
        Map<Integer, ConcurrentLinkedQueue<Answer>> answerMap = QuestionUtils.getAnswerMap(id);
        ConcurrentLinkedQueue<Answer> answersList = answerMap.get(answer);
        answersList.add(userAnswer);


        if (overTime&&answer != null&&question.getRightAnswer().equals(answer)) {

            userAnswer.setIsRight(true);
            user.setScore(user.getScore()+1);
            if (id == HeapVariable.questionsList.size()) {
                 grid.setErrorCode("-1");
                 grid.setMessage("you win");

            }else{

                grid.setErrorCode("0");
                grid.setMessage("next question");
            }

        }else if (!overTime) {
             user.setDieIndex(id);
             grid.setErrorCode("26");
             grid.setMessage("time over "+HeapVariable.intervalSecond+" seconds");
        }else{
             user.setDieIndex(id);
             grid.setErrorCode("20");
             grid.setMessage("lost");
        }


        return grid;
    }


}
