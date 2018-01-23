package com.xyl.game.Service.impl;

import com.xyl.game.Service.InitService;
import com.xyl.game.po.Answer;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.QuestionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * InitServiceImpl
 *
 * @author Naah
 * @date 2018-01-21
 */
@Service
public class InitServiceImpl implements InitService {
    private final static Logger log = Logger.getLogger(InitServiceImpl.class);

    @Override
    public GridPage initGame(String sessionId, User user) {
        GridPage result = new GridPage();
        result.setMethod(FinalVariable.INIT_METHOD);
        log.info("初始化用户：" + user.getUsername() + " 开始");
        try {
            if (HeapVariable.beginTime == null) {
                result.setErrorCode(FinalVariable.NO_GAME_STATUS_CODE);
                result.setMessage(FinalVariable.NO_GAME_MESSAGE);
                return result;
            }
            if (HeapVariable.usersMap.containsKey(sessionId)) {
                log.info("初始化用户：" + user.getUsername() + " 已存在");

                User u = HeapVariable.usersMap.get(sessionId);
                if (u.getDieIndex() != null) {
                    result.setErrorCode(FinalVariable.GAME_DONE_STATUS_CODE);
                    result.setMessage(FinalVariable.GAME_DONE_MESSAGE+" 你的最高分数：" + u.getScore() + "！");
                    return result;
                } else {
                    result.setErrorCode(FinalVariable.YOU_EXIT_STATUS_CODE);
                    result.setMessage(FinalVariable.YOU_EXIT_MESSAGE);
                }
                if (u.getScore() == HeapVariable.questionsList.size()) {
                    result.setErrorCode(FinalVariable.YOU_WIN_STATUS_CODE);
                    result.setMessage(FinalVariable.YOU_WIN_MESSAGE);
                }
                return result;
            } else if (HeapVariable.now == null) {
                user.setSessionId(sessionId);
                user.setScore(0);
                user.setTimesSecond(0);
                user.setAnswers(new ArrayList<Answer>(HeapVariable.questionsList.size()));
                HeapVariable.usersMap.put(sessionId, user);
                result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
                result.setMessage(HeapVariable.beginTime.getTime() + "");
                result.setPageList(QuestionUtils.getNowQuestionDTOPage());
                log.info("初始化用户：" + user.getUsername() + " 成功");
            } else {
                result.setErrorCode(FinalVariable.GAME_HAS_STARTED_STATUS_CODE);
                result.setMessage(FinalVariable.GAME_HAS_STARTED_MESSAGE);
            }

        } catch (Exception e) {
            log.info("初始化用户异常：" + user.getUsername());
            result.setErrorCode(FinalVariable.INIT_ERROR_STATUS_CODE);
            result.setMessage(FinalVariable.INIT_ERROR_MESSAGE);
            e.printStackTrace();
            return result;
        }
        return result;
    }
}
