package com.jiayaxing.web.config.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

public class MyLogoutFilter extends LogoutFilter{
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		 //在这里可以执行退出系统前需要清空的作者自己缓存的数据（不包括权限缓存数据，因为权限缓存数据shiro自己管理了，shiro自己会删除。）异常退出的话需要用SessionListener去清除。

		Subject subject = getSubject(request, response);  
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {  
            subject.logout();  
        } catch (Exception ise) {  
            ise.printStackTrace();  
        }  
        issueRedirect(request, response, redirectUrl);
		return false;
	}
}
