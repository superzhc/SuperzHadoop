package com.github.superzhc.flink.manage.controller;


import com.github.superzhc.flink.manage.entity.JobInfo;
import com.github.superzhc.flink.manage.service.IJobInfoService;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务信息 前端控制器
 * </p>
 *
 * @author superz
 * @since 2021-04-12
 */
@RestController
@RequestMapping("/job-info")
public class JobInfoController {
    @Autowired
    private IJobInfoService jobInfoService;

    @GetMapping
    public Result<List<JobInfo>> get() {
        List<JobInfo> lst = jobInfoService.list();
        return Result.success(lst);
    }

    @GetMapping("/{id}")
    public Result<JobInfo> get(@PathVariable int id) {
        JobInfo jobInfo = jobInfoService.getById(id);
        return Result.success(jobInfo);
    }

    @PostMapping
    public Result add(JobInfo jobInfo) {
        boolean b = jobInfoService.save(jobInfo);
        return b ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping
    public Result edit(JobInfo jobInfo) {
        boolean b = jobInfoService.updateById(jobInfo);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        boolean b = jobInfoService.removeById(id);
        return b ? Result.success("删除成功") : Result.fail("删除失败");
    }
}

