package com.xyl.game.Service.impl;

import com.xyl.game.Service.RankService;
import com.xyl.game.dto.UserDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.po.User;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.HeapVariable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * RankServiceImpl
 *
 * @author Naah
 * @date 2018-01-21
 */
@Service
public class RankServiceImpl implements RankService {

    @Override
    public GridPage<UserDTO> getRank() {
        Map<String, User> usersMap = HeapVariable.usersMap;
        Collection<User> users = usersMap.values();
          Page<UserDTO> userDTO=new Page<>(1,users.size());
        for (User user : users) {
            userDTO.add(new UserDTO(user.getUsername(),user.getDepartment(),user.getScore(),user.getTimesSecond()));
        }
        Collections.sort(userDTO);

        GridPage<UserDTO> result=new GridPage<>(userDTO);
        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);


        return result;
    }
}
