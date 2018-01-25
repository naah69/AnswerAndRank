package com.xyl.game.contrller;

import com.xyl.game.service.RankService;
import com.xyl.game.po.GridPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rankController
 *
 * @author Naah
 * @date 2018-01-21
 */
@RestController
@CrossOrigin
public class RankController {

    @Autowired
    private RankService service;

@GetMapping("rank")
    public GridPage getRank(){
      return  service.getRank();
}
}
