package com.xyl.game.Service.impl;

import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.Answer;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.QuestionUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * uploadSocreServiceImpl
 *
 * @author Naah
 * @date 2018-01-21
 */
@Service
public class UploadScoreServiceImpl implements UploadScoreService {
    @Override
    public GridPage<QuestionDTO> uploadScore(Integer id, Byte answer, Integer times, String sessionId, User user) {
        GridPage<QuestionDTO> result = new GridPage<>();
        result.setMethod(FinalVariable.UPLOAD_METHOD);
        Timestamp time = HeapVariable.beginTime;
        if (HeapVariable.beginTime == null || time.getTime() > System.currentTimeMillis()) {
            result.setErrorCode(FinalVariable.WAIT_STATUS_CODE);
            result.setMessage(time.getTime() + "");
            return result;
        }

        if (times == null) {
            result.setErrorCode(FinalVariable.NO_TIME_STATUS_CODE);
            result.setMessage(FinalVariable.NO_TIME_MESSAGE);
        }

        if (sessionId == null) {
            result.setMessage(FinalVariable.NO_SESSION_STATUS_CODE);
            result.setMessage(FinalVariable.NO_SESSION_MESSAGE);
        }

        if (user == null) {
            result.setErrorCode(FinalVariable.NO_USER_STATUS_CODE);
            result.setMessage(FinalVariable.NO_USER_MESSAGE);
        }


        if (user.getDieIndex() != null) {
            result.setErrorCode(FinalVariable.YOU_HAD_DIED_STATUS_CODE);
            result.setMessage(FinalVariable.YOU_HAD_DIED_MESSAGE);
        }


        if (!id.equals(HeapVariable.now.getId())) {
            result.setErrorCode(FinalVariable.DIFFERENT_QUESTION_STATUS_CODE);
            result.setMessage(FinalVariable.DIFFERENT_QUESTION_MESSAGE);
            user.setDieIndex(HeapVariable.now.getId());
        }

        if (result.getErrorCode() != null) {
            return result;
        }

        answer = answer != null ? answer : 0;
        AnnualMeetingGameQuestion question = QuestionUtils.getQuestion(id);
        Answer userAnswer = new Answer(id, question, answer, times, false, new Timestamp(System.currentTimeMillis()));
        user.getAnswers().add(userAnswer);
        user.setTimesSecond(user.getTimesSecond() + times);
        Map<Byte, AtomicInteger> answerMap = QuestionUtils.getAnswerMap(id);
        AtomicInteger count = answerMap.get(answer);
        count.incrementAndGet();


        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
        result.setMessage(FinalVariable.NORMAL_MESSAGE);

        return result;


    }


}
