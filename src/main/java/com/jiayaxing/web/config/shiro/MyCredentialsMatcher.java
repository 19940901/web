package com.jiayaxing.web.config.shiro;

import com.jiayaxing.web.config.shiro.redis.MyRedisCacheManager;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class MyCredentialsMatcher extends HashedCredentialsMatcher {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Cache<String, AtomicInteger> passwordRetryCache;

    public MyCredentialsMatcher(MyRedisCacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    //多次密码错误锁定账户
	/*@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		 String loginName = (String) token.getPrincipal();
	     AtomicInteger retryCount = passwordRetryCache.get(loginName);
         if(retryCount==null){
            retryCount = new AtomicInteger();
            passwordRetryCache.put(loginName, retryCount);
         }
         if(retryCount.incrementAndGet()>2){//第三次弹出多次错误，以后在缓存期内开始校验验证码的正确性。
             throw new ExcessiveAttemptsException();
         }
         boolean matchs = super.doCredentialsMatch(token, info);
         if(matchs){
             passwordRetryCache.remove(loginName);
         }
		 return matchs;
	}*/
    //多次密码错误启用验证码
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        System.out.println("=================domatch");
        String loginName = (String) token.getPrincipal();
        log.debug("get login name");
        AtomicInteger retryCount = passwordRetryCache.get(loginName);
        boolean matchs = super.doCredentialsMatch(token, info);
        if (matchs) {
            if (retryCount != null) {
                passwordRetryCache.remove(loginName);
            }
        } else {//密码错误
            if (retryCount == null) {
                retryCount = new AtomicInteger();
                passwordRetryCache.put(loginName, retryCount);
            }
            if (retryCount.incrementAndGet() > 2) {//第三次弹出多次错误，以后在缓存期内开始校验验证码的正确性。
                throw new ExcessiveAttemptsException();
            }
        }
        return matchs;
    }

    public Cache<String, AtomicInteger> getPasswordRetryCache() {
        return passwordRetryCache;
    }

    public void setPasswordRetryCache(Cache<String, AtomicInteger> passwordRetryCache) {
        this.passwordRetryCache = passwordRetryCache;
    }
}
