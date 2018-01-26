package com.xyl.game.contrller;

import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.service.AnnualMeetingQuestionExctFileSerivce;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 接收Exct文件
 * @author dazhi
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AnnualMeetingQuestionFileLoadContraller {

	private static final Logger logger = Logger.getLogger(AnnualMeetingQuestionFileLoadContraller.class);

	@Autowired
	private AnnualMeetingQuestionExctFileSerivce exctFileSerivce;

	/**
	 * 接收年会游戏上传的exct表格文件
	 */
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public AnnualMeetingGameQuestionVo uploadExct(@RequestParam(value="exctFile")MultipartFile multipartFile,HttpSession session){
		logger.info("excel文件上传");
		AnnualMeetingGameQuestionVo savaDataForExct = null;
		try {
			savaDataForExct = exctFileSerivce.savaDataForExct(multipartFile.getInputStream());
			logger.info("excel文件解析成功");
		} catch (IOException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		HeapVariable.annualMeetingGameQuestionVos.put(session.getId(), savaDataForExct);
		logger.info("excel文件数据保存内存");
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
		logger.info("修改问题的缓存数据");
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
		logger.info("查询缓存数据");
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
			logger.info("缓存没有数据");
			return "ok";
		}
		AnnualMeetingGameQuestionVo annualMeetingGameQuestionVo = annualMeetingGameQuestionVos.remove(session.getId());

		if(annualMeetingGameQuestionVo ==null ){
			logger.info("缓存没有数据");
			return "ok";
		}

		List<AnnualMeetingGameQuestion> allQuestions = annualMeetingGameQuestionVo.getAllQuestions();
		if(allQuestions == null){
			logger.info("缓存没有数据");
			return "ok";
		}

		if(exctFileSerivce.savaAnnualMeetingGameQuestion(allQuestions)){
			logger.info("存储成功");
			return "ok";
		}else{
			logger.info("存储失败");
			return "fail";
		}
	}

}
