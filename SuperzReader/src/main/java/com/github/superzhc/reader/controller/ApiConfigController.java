package com.github.superzhc.reader.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.superzhc.reader.common.ResultT;
import com.github.superzhc.reader.entity.ApiConfig;
import com.github.superzhc.reader.service.ApiConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author superz
 * @create 2021/8/18 21:01
 */
@RestController
@RequestMapping("/apiconfig")
@Slf4j
public class ApiConfigController {
    @Autowired
    private ApiConfigService apiConfigService;

    @GetMapping("/page")
    public ResultT page(@RequestParam Integer page, @RequestParam Integer limit) {
        IPage<ApiConfig> datas = apiConfigService.page(new Page<>(page, limit));
        return ResultT.success(datas);
    }

    @PostMapping
    public ResultT add(ApiConfig apiConfig) {
        // 判断数据服务地址是否存在
        boolean isExist = apiConfigService.exist(apiConfig.getPath());
        if (isExist) {
            return ResultT.fail("数据服务地址[{}]已存在", apiConfig.getPath());
        }

        boolean b = apiConfigService.save(apiConfig);
        return ResultT.msg(b ? 0 : 1, b ? "新增成功" : "新增失败");
    }
}
