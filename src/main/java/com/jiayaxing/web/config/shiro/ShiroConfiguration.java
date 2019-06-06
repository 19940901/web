package com.jiayaxing.web.config.shiro;

import com.jiayaxing.web.config.shiro.redis.MyRedisCacheManager;
import com.jiayaxing.web.config.shiro.redis.RedisSessionDao;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.Filter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {


    @Autowired
    MyRedisCacheManager myRedisCacheManager;
    @Autowired
    RedisSessionDao redisSessionDAO;



    /*@Bean(name = "ehcache")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("/config/ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }*/

    /*设置spring的缓存*/
/*	@Bean//(name = "springEhCacheManager")
	public EhCacheCacheManager ehCacheCacheManager(){  
	    EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();  
	    ehCacheCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
	    return ehCacheCacheManager;  
	}*/

    /**
     * 设置shiro的缓存由ehcache缓存用户的权限数据，用户退出或者会话失效后shiro会调用SessionDAO的delete删除缓存的用户权限数据
     *
     * @return
     */
    /*@Bean//(name = "shiroEhCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return ehCacheManager;
    }*/


    //原生加密器
	/*public HashedCredentialsMatcher  credentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("md5");
		credentialsMatcher.setHashIterations(3);
		return credentialsMatcher;
	}*/
    //自定义加密器
    public HashedCredentialsMatcher credentialsMatcher() {
        MyCredentialsMatcher credentialsMatcher = new MyCredentialsMatcher(myRedisCacheManager);
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(3);
        return credentialsMatcher;
    }


    //将自己的验证方式加入容器
    @Bean
    public MyShiroRealm myShiroRealm() {
        System.out.println("=====================realm");
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCachingEnabled(false);
        myShiroRealm.setCredentialsMatcher(credentialsMatcher());
        return myShiroRealm;
    }

    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(redisSessionDAO);
        mySessionManager.setGlobalSessionTimeout(18000000);//设置Session失效时间5个小时
        mySessionManager.setSessionValidationInterval(18000000);//设置扫描间隔时间
        mySessionManager.setDeleteInvalidSessions(true);//在会话过期后会调用SessionDAO的delete方法删除会话：如会话时持久化存储的，可以调用此方法进行删除。
        mySessionManager.setSessionValidationSchedulerEnabled(true);//使能扫描调度器
        return mySessionManager;
    }


    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(myRedisCacheManager);
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager(simpleCookie()));
        return securityManager;
    }

    /*rememberMe*/
    @Bean
    public RememberMeManager rememberMeManager(SimpleCookie simpleCookie) {

        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(Base64.getDecoder().decode("Z3VucwAAAAAAAAAAAAAAAA=="));
        rememberMeManager.setCookie(simpleCookie);
        return rememberMeManager;
    }

    /*cookie*/
    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("REMEMBERME");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(7 * 24 * 3600);
        return simpleCookie;
    }

    //Filter工厂，配置Shiro的拦截器。设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> mapFilter = new LinkedHashMap<>();
        MyLogoutFilter myLogoutFilter = new MyLogoutFilter();
        RolesAuthorizationFilter rolesAuthorizationFilter = new RolesAuthorizationFilter();
        MyAccessControlFilter accessFilter = new MyAccessControlFilter();
        accessFilter.setCacheManager(myRedisCacheManager);
        myLogoutFilter.setRedirectUrl("/registerController/logout1");//设置退出后URL跳转
        mapFilter.put("logout", myLogoutFilter);
        mapFilter.put("RolesOr", rolesAuthorizationFilter);
        mapFilter.put("accessFilter", accessFilter);
        shiroFilterFactoryBean.setFilters(mapFilter);
        //如果没有登录，跳转的地址
        shiroFilterFactoryBean.setLoginUrl("/registerController/returnLogin");
        //登录成功，跳转的地址。这里不需要设置登录成功后的页面。因为shiro默认跳转到上一次客户浏览的页面。
        //shiroFilterFactoryBean.setSuccessUrl("/registerController/loginSuccess");
        //权限不足验证失败，跳转的地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/registerController/unauthorized");

        Map<String, String> map = new LinkedHashMap<>();
        //登出
        map.put("/registerController/logout", "logout");//设置退出的URL，可以不用编写这个URL方法，直接用LogoutFilter拦截。
        // 配置不会被拦截的链接 顺序判断  ，用匿名过滤器拦截
        map.put("/vue","anon");

        map.put("/registerController/**", "anon");
        map.put("/public/**", "anon");
        map.put("/user/**", "anon");
        map.put("/auth/reminder","authc");
        map.put("/api/orderController/**", "authc");
        //map.put("/infoController/**", "authc,roles[admin]");  
        //对剩余的所有路径进行用户认证
        map.put("/infoController/getInfo", "authc");
        map.put("/infoController/getInfo1", "authc,RolesOr[\"admin\",\"user\",\"guest\"],accessFilter");//需要登录，管理员和游客角色都可以
        map.put("/infoController/getInfo2", "authc,RolesOr[admin],accessFilter");//需要登录，只有管理员可以
        map.put("/infoController/getInfo3", "authc,perms[\"employee:delete\"],accessFilter");//authc表示需要认证(登录)才能使用,只要登录就可以使用

        System.out.println("================================");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
