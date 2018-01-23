package com.xyl.game.Service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.game.Service.AuthenticationSerivce;
import com.xyl.game.utils.HeapVariable;

/**
 * 后台登录认证
 * @author dazhi
 *
 */
@Service
public class AuthenticationSerivceImpl implements AuthenticationSerivce{

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationSerivceImpl.class);
	/**
	 * 判断后台是否是第一次登录
	 */
	@Override
	public boolean isFirstAdmin() {
		if(HeapVariable.isFristAdmin){
			//是第一次登录
			logger.info("第一次登录");
			return true;
		}
		
		return false;
	}

}
