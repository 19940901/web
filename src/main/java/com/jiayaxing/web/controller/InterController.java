package com.jiayaxing.web.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InterController {

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String login() {
        return "user/login";
    }

    @RequestMapping(value = "vue", method = RequestMethod.GET)
    public String vue() {
        return "vue";
    }

    @RequestMapping(value = "/auth/reminder", method = RequestMethod.GET)
    public String reminder() {
        Subject subject = SecurityUtils.getSubject();
        boolean authenticated = subject.isAuthenticated();
        if (authenticated)
            return "auth/reminder";
        else return "user/login";
    }
}
