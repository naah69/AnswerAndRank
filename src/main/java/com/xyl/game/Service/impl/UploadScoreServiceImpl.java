package com.xyl.game.Service.impl;

import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.*;
import com.xyl.game.utils.HeapVariable;
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
    public GridPage<QuestionDTO> uploadScore(Integer id, String answer, Integer times, String sessionId, User user) {
        GridPage<QuestionDTO> grid = new GridPage<>();
        if (id == null) {
            grid.setErrorCode("21");
            grid.setMessage("id not fouond");

        }
        if (answer == null) {
            grid.setErrorCode("22");
            grid.setMessage("answer not found");
        }

        if (times == null) {
            grid.setErrorCode("23");
            grid.setMessage("time not found");
        }

        if (sessionId == null) {
            grid.setMessage("24");
            grid.setMessage("session not found");
        }

        if (user == null) {
            grid.setErrorCode("25");
            grid.setMessage("user not found");
        }
        List<AnnualMeetingGameQuestion> questionsList = HeapVariable.questionsList;
        if (id > questionsList.size()) {
            grid.setErrorCode("26");
            grid.setMessage("index over flow");

        }
        if (user.getDieIndex() != null) {
            grid.setErrorCode("27");
            grid.setMessage("you had died");
        }

        if (grid.getErrorCode()!=null) {
            return grid;
        }


        AnnualMeetingGameQuestion question = questionsList.get(id - 1);
        Answer userAnswer = new Answer(id, question, answer, times,false);
        user.getAnswers().add(userAnswer);
        user.setTimesSecond(user.getTimesSecond() + times);

        if (question.getRightAnswer().equals(answer)) {
            userAnswer.setIsRight(true);
            user.setScore(user.getScore()+1);
            if (id == questionsList.size()) {
                 grid.setErrorCode("-1");
                 grid.setMessage("you win");

            }else{
                List<QuestionDTO> questionDTOList = HeapVariable.questionDTOList;
                QuestionDTO dto=questionDTOList.get(id);
                Page<QuestionDTO> page=new Page<>();
                page.set(dto.getId(),dto);
                grid.setErrorCode("0");
                grid.setMessage("next question");
                grid.setPageList(page);
            }

        }else{
             user.setDieIndex(id);
             grid.setErrorCode("20");
             grid.setMessage("lost");
        }


        return grid;
    }
}
