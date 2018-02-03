package com.xyl.game.utils;

import com.xyl.game.dto.UserDTO;
import com.xyl.game.po.Page;
import com.xyl.game.po.User;
import com.xyl.game.websocket.AnswerWebSocket;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * UserUtils
 *
 * @author Naah
 * @date 2018-01-23
 */
public class UserUtils {
    /**
     * 获取用户数据
     * @param session
     * @return
     */
    public static User getUser(AnswerWebSocket session){
        if(session.tel!=null){
             User user = HeapVariable.usersMap.get(session.tel);
            return user;
        }else{
            return null;
        }

    }

    /**
     * 排行榜
     * @return
     */
    public static  Page<UserDTO> getRank(){
         Map<String, User> usersMap = HeapVariable.usersMap;
        Collection<User> users = usersMap.values();
          Page<UserDTO> userDTO=new Page<>(1,users.size());
        for (User user : users) {
            userDTO.add(new UserDTO(user.getUsername(),user.getDepartment(),user.getScore(),user.getTimesSecond()));
        }
        Collections.sort(userDTO);
        return userDTO;
    }
}
