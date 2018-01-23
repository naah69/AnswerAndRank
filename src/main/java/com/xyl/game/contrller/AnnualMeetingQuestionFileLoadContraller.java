package com.xyl.game.contrller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyl.game.Service.AnnualMeetingQuestionExctFileSerivce;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;

import scala.sys.process.ProcessBuilderImpl.Simple;

/**
 * 接收Exct文件
 * @author dazhi
 *
 */
@Controller
@RequestMapping("/admin")
public class AnnualMeetingQuestionFileLoadContraller {
	
	private static final Logger logger = LoggerFactory.getLogger(AnnualMeetingQuestionFileLoadContraller.class);

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
		
		HeapVariable.annualMeetingGameQuestionVos.put(session.getId(), savaDataForExct);
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
		AnnualMeetingGameQuestionVo attribute = HeapVariable.annualMeetingGameQuestionVos.get(session.getId());
		try {
			if(exctFileSerivce.updataData(attribute, id, fieldValue, fieldName)){
				logger.info("年会问题缓存数据修改成功");
			}else{
				logger.info("年会问题缓存数据未修改");
			}
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		HeapVariable.annualMeetingGameQuestionVos.put(session.getId(), attribute);
		
		return "OK";
	}
	
	/**
	 * 查询数据库以及存储的数据
	 */
	@RequestMapping("/userData")
	@ResponseBody
	public AnnualMeetingGameQuestionVo getAllGameQuestion(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return exctFileSerivce.getAllGameQuestion();
		//return null;
	}
	
	/**
	 * 保存年会问题的数据
	 * @return
	 */
	@RequestMapping("/savaAnnualMeetingGameQuestion")
	@ResponseBody
	public String savaAnnualMeetingGameQuestion(HttpSession session){
		//获得数据
		Map<String, AnnualMeetingGameQuestionVo> annualMeetingGameQuestionVos = HeapVariable.annualMeetingGameQuestionVos;
		if(annualMeetingGameQuestionVos == null){
			return "ok";
		}
		AnnualMeetingGameQuestionVo annualMeetingGameQuestionVo = annualMeetingGameQuestionVos.remove(session.getId());
		
		if(annualMeetingGameQuestionVo ==null ){
			return "ok";
		}
		
		List<AnnualMeetingGameQuestion> allQuestions = annualMeetingGameQuestionVo.getAllQuestions();
		if(allQuestions == null){
			return "ok";
		}
		
		if(exctFileSerivce.savaAnnualMeetingGameQuestion(allQuestions)){
			return "ok";
		}else{
			return "fail";
		}
	}
	
	/**
	 * 清除所有数据
	 * @return
	 */
	@RequestMapping("/clearAllData")
	@ResponseBody
	public String clearAllData(){
		HeapVariable.atomic.set(0);
		exctFileSerivce.clearAllData();
		return "ok";
	}
	
	@RequestMapping("/time")
	@ResponseBody
	public String time(String time){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date times = null;
		try {
			times = dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		HeapVariable.beginTime = new Timestamp(times.getTime());
		return "ok";
	}
}
