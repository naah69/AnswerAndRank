package com.xyl.game.Service.impl;

import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.*;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.QuestionUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * uploadSocreServiceImpl
 *
 * @author Naah
 * @date 2018-01-21
 */
@Service
public class UploadScoreServiceImpl implements UploadScoreService {
    @Override
    public GridPage<QuestionDTO> uploadScore(Byte answer, Integer times, String sessionId, User user) {
        GridPage<QuestionDTO> grid = new GridPage<>();
        List<Answer> answers = user.getAnswers();
        int id = answers.size()+1;


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

        List<AnnualMeetingGameQuestion> questionsList = HeapVariable.questionsList;
        if (id > questionsList.size()) {
            grid.setErrorCode("24");
            grid.setMessage("index over flow");
        }

        if (user.getDieIndex() != null) {
            grid.setErrorCode("25");
            grid.setMessage("you had died");
        }

        if (grid.getErrorCode()!=null) {
            return grid;
        }



        AnnualMeetingGameQuestion question = questionsList.get(id - 1);
        Answer userAnswer = new Answer(id, question, answer!=null?answer:0, times,false,new TimeStamp(System.currentTimeMillis()));
        boolean overTime=answers.size()>id?userAnswer.getCommitTime().getTime()<answers.get(answers.size() - 1).getCommitTime().getTime()+20000:times<20;
        answers.add(userAnswer);
        user.setTimesSecond(user.getTimesSecond() + times);

        if (overTime&&answer != null&&question.getRightAnswer().equals(answer)) {

            userAnswer.setIsRight(true);
            user.setScore(user.getScore()+1);
            if (id == questionsList.size()) {
                 grid.setErrorCode("-1");
                 grid.setMessage("you win");

            }else{

                grid.setErrorCode("0");
                grid.setMessage("next question");
                grid.setPageList(QuestionUtils.getNextQuestion(id));
            }

        }else if (!overTime) {
             user.setDieIndex(id);
             grid.setErrorCode("26");
             grid.setMessage("time over 10 seconds");
        }else{
             user.setDieIndex(id);
             grid.setErrorCode("20");
             grid.setMessage("lost");
        }


        return grid;
    }


}
