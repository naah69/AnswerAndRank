package com.xyl.game.Service.impl;

import com.xyl.game.Service.InitService;
import com.xyl.game.po.Answer;
import com.xyl.game.po.User;
import com.xyl.game.utils.HeapVariable;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * InitServiceImpl
 *
 * @author Naah
 * @date 2018-01-21
 */
@Service
public class InitServiceImpl implements InitService {
    private  final static Logger log=Logger.getLogger(InitServiceImpl.class);
    @Override
    public int initGame(HttpServletRequest request, User user) {
        log.info("初始化用户："+user.getUsername());
        try {
            String sessionId = request.getSession().getId();
            user.setSessionId(sessionId);
            user.setScore(0);
            user.setTimesSecond(0);
            user.setAnswers(new ArrayList<Answer>(HeapVariable.questionsList.size()));
            HeapVariable.usersMap.put(sessionId,user);
        } catch (Exception e) {
            log.info("初始化用户异常："+user.getUsername());
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
