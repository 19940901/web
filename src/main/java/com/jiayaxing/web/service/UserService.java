package com.jiayaxing.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jiayaxing.web.dao.ShiroRolePermissonMapper;
import com.jiayaxing.web.dao.ShiroUserMapper;
import com.jiayaxing.web.dao.ShiroUserRoleMapper;
import com.jiayaxing.web.model.ShiroUser;

@Service("userService")
public class UserService {

    @Autowired
    ShiroUserMapper shiroUserMapper;
    @Autowired
    ShiroUserRoleMapper shiroUserRoleMapper;
    @Autowired
    ShiroRolePermissonMapper shiroRolePermissonMapper;

    public Map<String, Object> getUserInfoByUsername(String name) {
        Map<String, Object> map = shiroUserMapper.getUserInfoByUsername(name);
        return map;
    }

    @Cacheable(cacheNames = {"user", "user"},key = "#name +\"rols\"")
    public Set<String> getRolesByUsername(String name) {
        Set<String> list = shiroUserRoleMapper.getRolesByUsername(name);
        return list;
    }

    @Cacheable(cacheNames = {"user", "user"},key = "#name +\"perm\"")
    public Set<String> getPermsByUsername(String name) {
        Set<String> list = shiroRolePermissonMapper.getPermsByUsername(name);
        return list;
    }

    @Cacheable(cacheNames = {"user", "user"}, key = "#id", cacheManager = "redisCacheManager")
    public ShiroUser getUserInfo(int id) {
        System.out.println("数据库查询");
        ShiroUser shiroUser = shiroUserMapper.selectByPrimaryKey(id);
        return shiroUser;
    }

    public int add(ShiroUser user) {
        return shiroUserMapper.insert(user);
    }
}
