package com.xyl.game.utils;

import com.xyl.game.po.User;
import com.xyl.game.websocket.AnswerWebSocket;

/**
 * UserUtils
 *
 * @author Naah
 * @date 2018-01-23
 */
public class UserUtils {
    public static User getUser(AnswerWebSocket session){
        String sessionId=session.getSessionId();
        User user = HeapVariable.usersMap.get(sessionId);
        return user;
    }
}
