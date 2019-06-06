package com.jiayaxing.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiayaxing.web.model.ShiroUser;
import com.jiayaxing.web.service.EhCacheService;
import com.jiayaxing.web.service.UserService;

@RestController
@RequestMapping("infoController")
public class InfoController {

    private static Logger log = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    UserService userService;
    @Autowired
    EhCacheService ehCacheService;

    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    public ShiroUser getInfo(@RequestParam String username) {
        ShiroUser map =  userService.getUserInfo(Integer.valueOf(username));
       // log.debug(o.getClass().getName());
        return map;
    }

    @RequestMapping(value = "getInfo1", method = RequestMethod.POST)
    public ShiroUser getInfo1(@RequestParam String username) {

        ShiroUser map = userService.getUserInfo(Integer.valueOf(username));
        ehCacheService.saveRegisterInfo("12345", "asdfgh");
        return map;
    }

    @RequestMapping(value = "getInfo2", method = RequestMethod.POST)
    public ShiroUser getInfo2(@RequestParam String username) {
        ShiroUser map = userService.getUserInfo(Integer.valueOf(username));
        String registerInfo = ehCacheService.getRegisterInfo("12345");
        System.out.println(registerInfo);
        return map;
    }

    @RequestMapping(value = "getInfo3", method = RequestMethod.POST)
    public ShiroUser getInfo3(@RequestParam String username) {
        ShiroUser map = userService.getUserInfo(Integer.valueOf(username));
        return map;
    }
}
