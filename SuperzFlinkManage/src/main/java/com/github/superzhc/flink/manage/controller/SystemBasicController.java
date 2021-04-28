package com.github.superzhc.flink.manage.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.superzhc.flink.manage.entity.SystemBasic;
import com.github.superzhc.flink.manage.service.ISystemBasicService;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * 系统基本信息 前端控制器
 * </p>
 *
 * @author superz
 * @since 2021-04-23
 */
@RestController
@RequestMapping("/system-basic")
public class SystemBasicController {
    @Autowired
    private ISystemBasicService systemBasicService;

    @GetMapping
    public Result get() {
        SystemBasic systemBasic = systemBasicService.getOne(new QueryWrapper<SystemBasic>());
        return Result.success(systemBasic);
    }

    @PutMapping
    public Result update(SystemBasic systemBasic) {
        boolean b = systemBasicService.updateById(systemBasic);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("Logo 不能为空");
        }

        try {
            String path = StrUtil.format("{}/images/logo/{}{}", this.getClass().getResource("/").getPath(), IdUtil.fastSimpleUUID(), file.getOriginalFilename());
            file.transferTo(new File(path));
            return Result.<String>success(path);
        } catch (Exception e) {
            return Result.fail(e);
        }
    }
}

