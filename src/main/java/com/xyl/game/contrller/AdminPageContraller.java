package com.xyl.game.contrller;

import com.xyl.game.service.AuthenticationSerivce;
import com.xyl.game.utils.HeapVariable;
import org.apache.log4j.Logger;
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
 * 后台页面访问控制
 * @author dazhi
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminPageContraller {

	private static final Logger logger=Logger.getLogger(AdminPageContraller.class);

	@Autowired
	private AuthenticationSerivce authenticationSerivce;

	/**
	 * 访问主页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("")
	public String getIndexPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("进入后台主页");
		return "Main";
	}

	/**
	 * 访问excel上传页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/loadPage")
	public String getLoadPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("访问上传excel文件页面");
		return "AnnualMeetingLoadExctFile";
	}

	/**
	 * 访问修改游戏时间参数的页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/setUpPage")
	public String getSetUpTimePage(HttpServletRequest request,HttpServletResponse response){
		logger.info("访问时间修改页面");
		return "AnnualTimeSetUp";
	}

	/**
	 * 访问登录页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/adminPage")
	public String getAdminPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("访问Admin页面");
		return "Admin";
	}

	@RequestMapping("/problemSwitchingPage")
	public String getproblemSwitchingPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("访问Admin页面");
		return "Admin";
	}

	/**
	 * 访问题目控制页面
	 * @return
	 */
	@RequestMapping("/selectPage")
	public String getTopicSelection(){
		logger.info("访问TopicSelection页面");
		return "TopicSelection";
	}

	/**
	 * 判断是否登录
	 * @return
	 */
	@RequestMapping("/isFirstAdmin")
	@ResponseBody
	public String isadmin(){
		logger.info("判断是否是第一次登录");
		if(authenticationSerivce.isFirstAdmin()){
			return UUID.randomUUID().toString();
		}
		return "no";
	}



	@RequestMapping("/admin")
	@ResponseBody
	public String admin(String pwd,HttpSession session){
		logger.info("用户进入登录逻辑");
		//登录操作
		if(authenticationSerivce.admin(pwd)){
			logger.info("用户已经登录！");
			session.setAttribute("user", HeapVariable.pwd);
			return "ok";
		}
		logger.info("登录失败");
		return "no";
	}
}
