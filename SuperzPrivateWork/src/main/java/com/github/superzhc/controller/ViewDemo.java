package com.github.superzhc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2021年01月22日 superz add
 */
@RestController
@RequestMapping("demo")
public class ViewDemo {

    /**
     * 返回单条数据
     * @return 单条数据
     */
    @RequestMapping("one")
    public Map<String,Object> getOneData(){
        Map<String,Object> map=new HashMap<>();
        map.put("k1","v1");
        map.put("k2","v2");
        return map;
    }
}
