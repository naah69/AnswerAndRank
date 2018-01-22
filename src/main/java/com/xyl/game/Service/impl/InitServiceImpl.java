package com.xyl.game.Service.impl;

import com.xyl.game.Service.InitService;
import com.xyl.game.po.Answer;
import com.xyl.game.po.GridPage;
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
    public GridPage initGame(HttpServletRequest request, User user) {
        GridPage result=new GridPage();
        log.info("初始化用户："+user.getUsername()+" 开始");
        try {
            String sessionId = request.getSession().getId();
            if (HeapVariable.usersMap.containsKey(sessionId)) {
                log.info("初始化用户："+user.getUsername()+" 失败");
                result.setErrorCode("11");
                result.setMessage("inited failed! user had inited");
                return result;
            }
            user.setSessionId(sessionId);
            user.setScore(0);
            user.setTimesSecond(0);
            user.setAnswers(new ArrayList<Answer>(HeapVariable.questionsList.size()));
            HeapVariable.usersMap.put(sessionId,user);
            result.setErrorCode("0");
            result.setMessage("hello "+user.getUsername());
            log.info("初始化用户："+user.getUsername()+" 成功");
        } catch (Exception e) {
            log.info("初始化用户异常："+user.getUsername());
            result.setErrorCode("0");
            result.setMessage("初始化异常");
            e.printStackTrace();
            return result;
        }
        return result;
    }
}
