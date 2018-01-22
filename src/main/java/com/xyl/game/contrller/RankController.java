package com.xyl.game.contrller;

import com.xyl.game.Service.RankService;
import com.xyl.game.po.GridPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rankController
 *
 * @author Naah
 * @date 2018-01-21
 */
@RestController
public class RankController {

    @Autowired
    private RankService service;

@GetMapping("rank")
    public GridPage getRank(){

    return null;
}
}
