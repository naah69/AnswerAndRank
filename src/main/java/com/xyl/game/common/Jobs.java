package com.xyl.game.common;

import com.xyl.game.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;


/**
 * Jobs
 * 定时输出excel 任务
 * @author Naah
 * @date 2018-01-21
 */
@Component
public class Jobs {
    public final static long FIRST_DELAY =  6 * 1000;
    public final static long ONE_MINUTE =  6 * 1000;

    public final static String USERDATA ="userData";
    public final static String FILESUFFIX = ".xlsx";

    private static final Logger logger = LoggerFactory.getLogger(Jobs.class);

    @Scheduled(initialDelay=FIRST_DELAY,fixedDelay=ONE_MINUTE)
    public void fixedDelayJob(){
    	File file = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()+USERDATA+FILESUFFIX);
    	ExcelUtil.savaUserData(file);
    }



}
