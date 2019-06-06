package com.jiayaxing.web.config.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class RolesAuthorizationFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("=================auth");
        Subject subject = getSubject(request, response);
    //    System.out.println(subject.hasRole("admin")+"-"+subject.hasRole("user")+"-"+subject.hasRole("guest"));
        String[] rolesArray = (String[]) mappedValue;
    //    System.out.println(rolesArray[0]+rolesArray[1]+rolesArray[2]);
        if (rolesArray == null || rolesArray.length == 0) { //没有角色限制，有权限访问
            return true;
        }
        for (int i = 0; i < rolesArray.length; i++) {

            if (subject.hasRole(rolesArray[i])) { //若当前用户是rolesArray中的任何一个，则有权限访问
                return true;
            }
        }
        return false;
    }


}
