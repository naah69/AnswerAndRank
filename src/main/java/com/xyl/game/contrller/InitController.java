package com.xyl.game.contrller;

import com.xyl.game.Service.InitService;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * InitController
 *
 * @author Naah
 * @date 2018-01-21
 */

@RestController
public class InitController {

    @Autowired
    private InitService service;

    @GetMapping("initGame")
    public GridPage init(User user, HttpServletRequest request) {
        int result = service.initGame(request, user);
        GridPage res=new GridPage();
        switch (result) {
            case 1:
                res.setMessage("success");
                break;
            default:
                res.setErrorCode("1");
                res.setMessage("init Failed");
                break;
        }
        return res;
    }
}
