package com.xyl.game.contrller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyl.game.Service.ExctFileSerivce;
/**
 * 接收Exct文件
 * @author dazhi
 *
 */
@Controller
public class FileLoadContraller {
	
	@Autowired 
	private ExctFileSerivce exctFileSerivce;
	
	@RequestMapping("/loadExctFile")
	@ResponseBody
	public String uploadExct(@RequestParam(value="exctFile")MultipartFile multipartFile){
		
		return null;
	}
	
}
