package com.jiayaxing.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.jiayaxing.web.model.ShiroUser;
import com.jiayaxing.web.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jiayaxing.web.service.EhCacheService;

import javax.servlet.http.HttpServletRequest;

import static org.apache.shiro.web.util.WebUtils.getSavedRequest;


@Controller
@RequestMapping("/registerController")
public class RegisterController {
    private static Logger log = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    EhCacheService ehCacheService;

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        Md5Hash hash = new Md5Hash(password, "yanzhi", 3);
        System.out.println(hash.toString());
        log.info(username + ":" + password);
        ShiroUser user = new ShiroUser();
        System.out.println(username + password);
        user.setUserName(username);
        user.setPassword(hash.toString());
        user.setLocked((byte) 0);
        user.setDeleted((byte) 0);
        userService.add(user);
        return map;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, Map<String, Object> map, @RequestParam String username, @RequestParam String password, String rememberme) {

        AtomicInteger passwordRetryTimes = ehCacheService.getPasswordRetryCache(username);
        if (passwordRetryTimes != null && passwordRetryTimes.get() > 2) {//第三次弹出多次错误，第四次开始校验验证码是否正确
            //校验验证码是否正确，正确则通过继续后续校验，不正确则直接返回不用校验后续校验。
            System.out.println("校验验证码");
        }
        System.out.println(passwordRetryTimes);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        SavedRequest req = null;
        if (Boolean.valueOf(rememberme))
            usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            log.info("=========login");
            map.put("token", subject.getSession().getId());
            req = WebUtils.getSavedRequest(request);
            map.put("msg", "登录成功");
        } catch (UnknownAccountException e) {
            map.put("msg", "账号或密码错误");//没找到帐号
        } catch (IncorrectCredentialsException e) {
            map.put("msg", "账号或密码错误");//密码错误
        } catch (LockedAccountException e) {
            map.put("msg", "用户已被冻结");//用户已被冻结
        } catch (ExcessiveAttemptsException e) {
            map.put("msg", "账号或密码错误次数太多，为了您的账户安全，账户被锁定十分钟。");//
        } catch (AuthenticationException e) {
            map.put("msg", "账号或密码错误");//其他认证失败如ExpiredCredentialsException 凭证过期、ConcurrentAccessException 并发访问异常（多个用户同时登录时抛出）、UnsupportedTokenException 使用了不支持的Token
        } catch (Exception e) {
            map.put("msg", "登录发生了错误");//发生了普通运行时异常
            log.error("错误信息", e);
        }

        String requestUrl = req.getRequestUrl();
        log.info("========" + requestUrl);
        if (requestUrl != null) {
            if (requestUrl.startsWith("/"))
                requestUrl=requestUrl.substring(1);
            return requestUrl;
        }
        return "vue";

    }


    @RequestMapping(value = "returnLogin", method = RequestMethod.GET)
    public String returnLogin() {

        return "user/login";
    }

    @ResponseBody
    @RequestMapping(value = "unauthorized", method = RequestMethod.GET)
    public Map<String, Object> unauthorized() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "权限不足，验证失败");
        log.info(map.toString());
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "logout1", method = RequestMethod.GET)
    public Map<String, Object> logout() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "退出登录");
        log.info(map.toString());
        return map;
    }


}
