package com.xyl.game.common;

import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Jobs
 * 定时输出excel 任务
 *
 * @author Naah
 * @date 2018-01-21
 */
@Component
public class Jobs {
    public final static long FIRST_DELAY = 30 * 1000;
    public final static long ONE_MINUTE = 6 * 1000;

    public final static String USERDATA = "userData";
    public final static String FILESUFFIX = ".xlsx";

    private static final Logger logger = Logger.getLogger(Jobs.class);

    @Autowired
    private StringRedisTemplate redis;

    @Scheduled(initialDelay = FIRST_DELAY, fixedDelay = ONE_MINUTE)
    public void fixedDelayJob() {
        //File file = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()+USERDATA+FILESUFFIX);
        //ExcelUtil.savaUserData(file);
        HashOperations<String, String, Object> redis = this.redis.opsForHash();
        redis.put("xyl", "usersMap", JsonUtils.objectToJSON(HeapVariable.usersMap));
        redis.put("xyl", "beginTime", JsonUtils.objectToJSON(HeapVariable.beginTime));
        redis.put("xyl", "now", JsonUtils.objectToJSON(HeapVariable.now));
        redis.put("xyl", "intervalSecond", JsonUtils.objectToJSON(HeapVariable.intervalSecond));
        redis.put("xyl", "answerList", JsonUtils.objectToJSON(HeapVariable.answerList));
        redis.put("xyl", "pwd", JsonUtils.objectToJSON(HeapVariable.pwd));
        redis.put("xyl", "isSendAnswer", JsonUtils.objectToJSON(HeapVariable.isSendAnswer));
        logger.info("备份数据完成");

    }


}
