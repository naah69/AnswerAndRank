package com.xyl.game.Service.impl;

import com.xyl.game.Service.InitService;
import com.xyl.game.po.Answer;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;
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
    public GridPage initGame( String sessionId , User user) {
        GridPage result = new GridPage();
        log.info("初始化用户：" + user.getUsername() + " 开始");
        try {
            if (HeapVariable.usersMap.containsKey(sessionId)) {
                log.info("初始化用户：" + user.getUsername() + " 已存在");

                User u = HeapVariable.usersMap.get(sessionId);
                if (u.getDieIndex() != null) {
                    result.setErrorCode("12");
                    result.setMessage("游戏已经结束！最高分数：" + u.getScore() + "！");
                    return result;
                } else {
                    result.setErrorCode("13");
                    result.setMessage("you had exit");
                }
                if (u.getScore() == HeapVariable.questionsList.size()) {
                    result.setErrorCode("-1");
                    result.setMessage("you had won!");
                }
                return result;
            } else if(HeapVariable.now.getId()==1) {
                user.setSessionId(sessionId);
                user.setScore(0);
                user.setTimesSecond(0);
                user.setAnswers(new ArrayList<Answer>(HeapVariable.questionsList.size()));
                HeapVariable.usersMap.put(sessionId, user);
                result.setErrorCode("0");
                result.setMessage("hello " + user.getUsername());
                result.setPageList(QuestionUtils.getNowQuestion());
                log.info("初始化用户：" + user.getUsername() + " 成功");
            }else{
                 result.setErrorCode("14");
                 result.setMessage("sorry,you are late" );
            }

        } catch (Exception e) {
            log.info("初始化用户异常：" + user.getUsername());
            result.setErrorCode("0");
            result.setMessage("初始化异常");
            e.printStackTrace();
            return result;
        }
        return result;
    }
}
