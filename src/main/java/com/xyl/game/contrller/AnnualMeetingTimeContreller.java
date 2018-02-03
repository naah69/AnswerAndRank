package com.xyl.game.contrller;

import com.xyl.game.service.AnnualMeetingTimeSerivce;
import com.xyl.game.po.TimeParam;
import com.xyl.game.utils.ExcelUtil;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.InitData;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author dazhi
 *
 */
@Controller
@RequestMapping("/admin")
public class AnnualMeetingTimeContreller {
	private static final Logger logger = Logger.getLogger(AnnualMeetingTimeContreller.class);

	@Autowired
	private AnnualMeetingTimeSerivce timeSerivce;

	/**
	 * 清除所有数据
	 * @return
	 */
	@RequestMapping("/clearAllData")
	@ResponseBody
	public String clearAllData(){
		logger.info("清除游戏数据");
		HeapVariable.atomic.set(0);
		timeSerivce.clearAllData();
		return "ok";
	}

	@RequestMapping("/time")
	@ResponseBody
	public String time(String time){
		logger.info("初始化游戏开始时间");
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
		logger.info("游戏开始时间初始化成功！");
		InitData.initCount(true);
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
		logger.info("设置游戏间隔时间！");
		if(gametime != null){
			HeapVariable.intervalSecond = gametime;
			logger.info("游戏间隔时间设置成功！");
		}
		return "ok";
	}


	@RequestMapping("/clearData")
	@ResponseBody
	public String clearData(){
		InitData.initGame();
		return "ok";
	}

	@RequestMapping("/PrintData")
	@ResponseBody
	public void printdata(HttpServletResponse response){
		logger.info("打印一场游戏的数据");
		OutputStream outputStream = null;
		response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename="+"printUserData.xlsx");
		try {
			outputStream = response.getOutputStream();
			ExcelUtil.printUserData(outputStream);
		} catch (IOException e) {
			logger.info("流获得失败");
			e.printStackTrace();
		}finally {
			try {
				if(outputStream != null){
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
