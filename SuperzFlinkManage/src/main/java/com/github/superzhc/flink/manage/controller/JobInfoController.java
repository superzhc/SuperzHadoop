package com.github.superzhc.flink.manage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.superzhc.flink.manage.entity.JobInfo;
import com.github.superzhc.flink.manage.job.monitor.JobMonitor;
import com.github.superzhc.flink.manage.job.monitor.JobMonitorFactory;
import com.github.superzhc.flink.manage.service.IJobInfoService;
import com.github.superzhc.flink.manage.util.FrontListParams;
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

    @Autowired
    JobMonitorFactory jobMonitorFactory;

    @GetMapping
    public Result<IPage<JobInfo>> list(FrontListParams params) {
        IPage<JobInfo> lst = jobInfoService.page(params.page(), params.orderBy());
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

    @PostMapping("/batch-delete")
    public Result batchDelete(@RequestParam(value = "ids[]") Integer[] ids) {
        for (Integer id : ids) {
            boolean b = jobInfoService.removeById(id);
            if (!b) {
                return Result.fail("删除失败");
            }
        }
        return Result.success("删除成功");
    }

    @GetMapping("/status")
    public Result status(int id) {
        JobInfo jobInfo = jobInfoService.getById(id);
        if (null == jobInfo) {
            return Result.fail("未查到任务：[id={}]", id);
        }

        JobMonitor jobMonitor = jobMonitorFactory.getJobMonitor(jobInfo.getJobType());
        return Result.success("状态获取成功，状态为{}", jobMonitor.status(jobInfo.getJobApplicationId()));
    }
}

