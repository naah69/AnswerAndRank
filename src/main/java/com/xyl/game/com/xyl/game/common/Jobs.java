package com.xyl.game.com.xyl.game.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Jobs
 * 定时输出excel 任务
 * @author Naah
 * @date 2018-01-21
 */
@Component
public class Jobs {
    public final static long FIRST_DELAY =  60 * 1000;
    public final static long ONE_MINUTE =  60 * 1000;

    @Scheduled(initialDelay = FIRST_DELAY,fixedDelay=ONE_MINUTE)
    public void fixedDelayJob(){

    }


}
