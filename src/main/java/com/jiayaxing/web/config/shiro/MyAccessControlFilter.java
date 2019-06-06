package com.jiayaxing.web.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.jiayaxing.web.config.shiro.redis.MyRedisCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAccessControlFilter extends AccessControlFilter{
	private static Logger log = LoggerFactory.getLogger(MyAccessControlFilter.class);
	
	private Cache<String,AtomicInteger>  continueAccessTimesCache;
	private Cache<String,Date> lastAccessTimeCache; 
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		String path = WebUtils.toHttp(request).getServletPath();
		String host = WebUtils.toHttp(request).getRemoteAddr();
		System.out.println("==============assess");
		if(!checkChannelAccess(request,response)) {
			 log.info(host+"用户短时间内多次访问了"+path+"接口");
			 JSONObject jsonObject = new JSONObject();
			 jsonObject.put("state", "false");
			 jsonObject.put("code", "100");
			 jsonObject.put("message", "系统检测到您的账户异常，为了您的账号安全系统暂时锁定账号十分钟，请十分钟之后再操作。");
			 HttpServletResponse httpResponse = WebUtils.toHttp(response);
			 httpResponse.setHeader("content-type", "application/json;charset=UTF-8");
			 httpResponse.getWriter().append(jsonObject.toString());
			 return false;
		 }else {
			 return true;
		}
	}
	
	public void setCacheManager(MyRedisCacheManager cacheManager) {
		this.continueAccessTimesCache = cacheManager.getCache("continueAccessTimesCache");
		this.lastAccessTimeCache = cacheManager.getCache("lastAccessTimeCache");
	}
	/**
	 * 检测用户是否频繁访问某一方法，频繁访问返回false，没有返回true。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private boolean checkChannelAccess(ServletRequest request, ServletResponse response) throws Exception{
		
		 Subject subject = getSubject(request, response);
		 Session session = subject.getSession();
		 String host = session.getHost();
		 Date lastAccessTime = session.getLastAccessTime();
		 String path = WebUtils.toHttp(request).getServletPath();
		 String key = host + path;
		 int maxAccessTimes = 2;//如果1秒内访问超过这个数，则是频繁访问了。应该限制。
		 log.debug("access too much test");
		 if(lastAccessTimeCache.get(key) != null) {//已经因为多次访问被限制了，所以直接返回false继续限制。
			 return false; 
		 }

		 log.debug("too long break");
		 AtomicInteger continueAccessTimes = continueAccessTimesCache.get(key);
		 if(continueAccessTimes == null) {//continueAccessTimes存储时间即为最大间隔时间，continueAccessTimes为null表示很久没有访问了可以直接通过
			 continueAccessTimes = new AtomicInteger(1);
			 continueAccessTimesCache.put(key, continueAccessTimes);//最大存储时间为1秒。
			 return true;
		 }else {
			 if(continueAccessTimes.incrementAndGet() > maxAccessTimes) {
				 lastAccessTimeCache.put(key, lastAccessTime);
				 return false;
			 }else {
				 return true;
			}
		} 
	}
}
