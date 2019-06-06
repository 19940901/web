package com.jiayaxing.web.config.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jiayaxing.web.controller.RegisterController;

@RestControllerAdvice
public class AuthExceptionHandler {
	private static Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);
	
	@ExceptionHandler(value=Exception.class)
	public Object errorHandler(HttpServletRequest request,HttpServletResponse response,Exception e) {
		Map<String,Object> map = new HashMap<>();
		if(e.getClass() == UnauthorizedException.class) {
			map.put("msg", "没有权限，验证失败");
		}else {
			map.put("msg", "发生了错误");
			log.error("错误信息",e);
		}
		
		return map;
	}
}
