package com.xyl.game.com.xyl.game.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.xyl.game.lerInterceptor.AuthenticationInterceptor;

@Configuration
public class IerInterceptorConfig extends WebMvcConfigurerAdapter{
	 /**
     * 注册 拦截器
     */
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        // 多个拦截器组成一个拦截器链
	        // addPathPatterns 用于添加拦截规则
	        // excludePathPatterns 用户排除拦截
	        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns("/admin/**");
	        
	        super.addInterceptors(registry);
	    }
	 
}
