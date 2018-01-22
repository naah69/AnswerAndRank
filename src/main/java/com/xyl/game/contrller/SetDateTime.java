package com.xyl.game.contrller;

import com.xyl.game.po.GridPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * SetDateTime
 *
 * @author Naah
 * @date 2018-01-22
 */
@RestController
public class SetDateTime {

    @GetMapping("setTime")
    public GridPage set(Timestamp time, HttpServletRequest request) {


        return null;
    }
}
