package com.github.superzhc.spring.web.controller;

import com.github.superzhc.common.http.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author superz
 * @create 2022/9/26 14:22
 **/
@RestController
@RequestMapping("/api/v1/flink/http")
public class FlinkHttpServerController {
    private static final Logger log = LoggerFactory.getLogger(FlinkHttpServerController.class);

    @PostMapping("/")
    public ResultT test(@RequestBody Map<String,Object> request) {
        log.info("request body:{}", request);
        return ResultT.success();
    }
}
