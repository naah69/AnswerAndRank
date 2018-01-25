package com.xyl.game.contrller;

import com.xyl.game.service.AuthenticationSerivce;
import com.xyl.game.utils.HeapVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 权限访问控制
 *
 * @author dazhi
 */
/**
 * 后台页面访问控制
 * @author dazhi
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminPageContraller {

	private static final Logger logger = LoggerFactory.getLogger(AdminPageContraller.class);

	@Autowired
	private AuthenticationSerivce authenticationSerivce;

	@RequestMapping("")
	public String getIndexPage(HttpServletRequest request,HttpServletResponse response){
		return "Main";
	}

	@RequestMapping("/loadPage")
	public String getLoadPage(HttpServletRequest request,HttpServletResponse response){
		return "AnnualMeetingLoadExctFile";
	}

	@RequestMapping("/setUpPage")
	public String getSetUpTimePage(HttpServletRequest request,HttpServletResponse response){
		return "AnnualTimeSetUp";
	}

	@RequestMapping("/adminPage")
	public String getAdminPage(HttpServletRequest request,HttpServletResponse response){
		return "Admin";
	}

	@RequestMapping("/problemSwitchingPage")
	public String getproblemSwitchingPage(HttpServletRequest request,HttpServletResponse response){
		return "Admin";
	}

	@RequestMapping("/selectPage")
	public String getTopicSelection(){
		return "TopicSelection";
	}

	@RequestMapping("/isFirstAdmin")
	@ResponseBody
	public String isadmin(){
		if(authenticationSerivce.isFirstAdmin()){
			return UUID.randomUUID().toString();
		}
		return "no";
	}



	@RequestMapping("/admin")
	@ResponseBody
	public String admin(String pwd,HttpSession session){
		//登录操作
		if(authenticationSerivce.admin(pwd)){
			logger.info("用户已经登录！");
			session.setAttribute("user", HeapVariable.pwd);
			return "ok";
		}
		return "no";
	}
}
