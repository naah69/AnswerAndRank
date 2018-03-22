package com.xyl.game.service.impl;

import com.xyl.game.service.RankService;
import com.xyl.game.dto.UserDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.UserUtils;
import org.springframework.stereotype.Service;

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
        Page<UserDTO> userDTO = UserUtils.getRank();

        GridPage<UserDTO> result=new GridPage<>(userDTO);
        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);


        return result;
    }
}
