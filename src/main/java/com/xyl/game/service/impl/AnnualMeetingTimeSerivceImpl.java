package com.xyl.game.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.game.service.AnnualMeetingTimeSerivce;
import com.xyl.game.po.TimeParam;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.InitData;
import com.xyl.game.utils.TimeFormatUtil;
/**
 *
 * @author dazhi
 *
 */
@Service
public class AnnualMeetingTimeSerivceImpl implements AnnualMeetingTimeSerivce {

	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionExctFileServiceImpl.class);

	@Override
	public void clearAllData() {
		InitData.initTable();
		InitData.initQuestion();
	}

	@Override
	public TimeParam getTimeParam() {
		Timestamp beginTime = HeapVariable.beginTime;
		TimeParam param = new TimeParam();
		if(beginTime != null){
			logger.info("游戏开始时间修改成功！");
			param.setBeginTime(beginTime.getTime());
			param.setBeginTimeStr(TimeFormatUtil.getTimeStr(new Date(beginTime.getTime())));
		}else{
			logger.info("游戏开始时间修未传入，未设置游戏开始时间！");
			param.setBeginTime(0L);
			param.setBeginTimeStr("未设置");
		}
		param.setIntervalTime(HeapVariable.intervalSecond);
		return param;
	}

}
