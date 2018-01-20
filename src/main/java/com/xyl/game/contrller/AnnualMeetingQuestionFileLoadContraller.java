package com.xyl.game.contrller;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyl.game.Service.AnnualMeetingQuestionExctFileSerivce;

/**
 * 接收Exct文件
 * @author dazhi
 *
 */
@Controller
public class AnnualMeetingQuestionFileLoadContraller {

	@Autowired 
	private AnnualMeetingQuestionExctFileSerivce exctFileSerivce;

	
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public String uploadExct(@RequestParam(value="exctFile")MultipartFile multipartFile){
		
		
		
		return null;
	}
	
}
