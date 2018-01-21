package com.xyl.game.contrller;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyl.game.Service.AnnualMeetingQuestionExctFileSerivce;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

/**
 * 接收Exct文件
 * @author dazhi
 *
 */
@Controller
public class AnnualMeetingQuestionFileLoadContraller {
	
	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionFileLoadContraller.class);
	
	public final static long FIRST_DELAY =  60 * 1000;
    public final static long ONE_MINUTE =  60 * 1000;

	@Autowired 
	private AnnualMeetingQuestionExctFileSerivce exctFileSerivce;
	
	/**
	 * 接收年会游戏上传的exct表格文件
	 */
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public AnnualMeetingGameQuestionVo uploadExct(@RequestParam(value="exctFile")MultipartFile multipartFile,HttpSession session){
		AnnualMeetingGameQuestionVo savaDataForExct = null;
		try {
			savaDataForExct = exctFileSerivce.savaDataForExct(multipartFile.getInputStream());
		} catch (IOException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		session.setAttribute(session.getId(), savaDataForExct);
		return savaDataForExct;
	}
	
	/**
	 * 修改缓存数据
	 * @param session
	 * @param id
	 * @param fieldValue
	 * @param fieldName
	 * @return
	 */
	@RequestMapping("/updataQuestionData")
	@ResponseBody
	public String updataQuestionData(HttpSession session,Integer id,String fieldValue,String fieldName){
		AnnualMeetingGameQuestionVo attribute = (AnnualMeetingGameQuestionVo)session.getAttribute(session.getId());
		try {
			if(exctFileSerivce.updataData(attribute, id, fieldValue, fieldName)){
				logger.info("年后问题缓存数据修改成功");
			}else{
				logger.info("年后问题缓存数据未修改");
			}
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		return "OK";
	}
	
	
	@Scheduled(initialDelay = FIRST_DELAY,fixedDelay=ONE_MINUTE)
	public void timedEritingWriteExcelFile(HttpServletRequest request){
		
		
	}
	
	
	/**
	 * 查询数据库以及存储的数据
	 *//*
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public AnnualMeetingGameQuestionVo getAllGameQuestion(){
		//return exctFileSerivce.getAllGameQuestion();
		return null;
	}*/
}
