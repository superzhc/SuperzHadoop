package com.github.superzhc.flink.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.superzhc.flink.manage.entity.SystemBasic;
import com.github.superzhc.flink.manage.service.ISystemBasicService;
import com.github.superzhc.flink.manage.service.ISystemMenuService;
import com.github.superzhc.flink.manage.util.CurrentUserUtil;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/22 18:10
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @Autowired
    ISystemBasicService systemBasicService;
    @Autowired
    ISystemMenuService systemMenuService;

    @GetMapping
    public Result basic() {
        Map<String, Object> map = new HashMap<>();

        // 获取当前用户信息
        map.put("userInfo", CurrentUserUtil.username());

        // 获取系统基本信息
        SystemBasic systemBasic = systemBasicService.getOne(new QueryWrapper<>());

        // 获取Logo信息
        Map<String, Object> logo = new HashMap<>(16);
        logo.put("title", systemBasic.getLogoTitle());
        logo.put("image", systemBasic.getLogoImage());
        logo.put("href", "");
        map.put("logoInfo", logo);

        // 获取Home信息
        Map<String, Object> home = new HashMap<>(16);
        home.put("title", systemBasic.getHomeTitle());
        home.put("href", systemBasic.getHomeUrl());
        map.put("homeInfo", home);

        // 获取菜单信息
        map.put("menuInfo", systemMenuService.menu());

        return Result.success(map);
    }
}
