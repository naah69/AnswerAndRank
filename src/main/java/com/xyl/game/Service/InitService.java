package com.xyl.game.Service;

import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * InitService
 * 初始化业务
 * @author Naah
 * @date 2018-01-21
 */
public interface InitService {
    /**
     * 初始化游戏
     * @param request
     * @param user
     * @return
     */
    public GridPage initGame(String sessionId, User user);
}
