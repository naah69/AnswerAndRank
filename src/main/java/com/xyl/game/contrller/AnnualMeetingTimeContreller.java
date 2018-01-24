package com.xyl.game.contrller;

import com.xyl.game.Service.AnnualMeetingTimeSerivce;
import com.xyl.game.po.TimeParam;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.InitData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author dazhi
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AnnualMeetingTimeContreller {
	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingTimeContreller.class);

	@Autowired
	private AnnualMeetingTimeSerivce timeSerivce;

	/**
	 * 清除所有数据
	 * @return
	 */
	@RequestMapping("/clearAllData")
	@ResponseBody
	public String clearAllData(){
		HeapVariable.atomic.set(0);
		timeSerivce.clearAllData();
		return "ok";
	}

	@RequestMapping("/time")
	@ResponseBody
	public String time(String time){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date times = null;
		try {
			times = dateFormat.parse(time);
		} catch (Exception e) {
			logger.info("时间戳未设置");
			e.printStackTrace();
			return "时间戳未设置！输入的格式有误";
		}

		HeapVariable.beginTime = new Timestamp(times.getTime());
		InitData.initCount();
		return "ok";
	}

	@RequestMapping("/getTimeParam")
	@ResponseBody
	public TimeParam getTime(){
		return timeSerivce.getTimeParam();
	}

	@RequestMapping("/setTimeInterval")
	@ResponseBody
	public String setTimeInterval(Integer gametime){
		if(gametime != null){
			HeapVariable.intervalSecond = gametime;
		}
		return "ok";
	}


	@RequestMapping("/clearData")
	@ResponseBody
	public String clearData(){
		InitData.initCount();
		HeapVariable.beginTime = null;
		return "ok";
	}

}
