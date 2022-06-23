package com.github.superzhc.web.base.controller;

import com.github.superzhc.common.http.ResultT;
import com.github.superzhc.web.base.dto.DemoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author superz
 * @create 2022/6/22 15:16
 **/
@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/view")
    public ResultT view(@RequestBody DemoDTO dto) {
        return ResultT.success(dto);
    }
}
