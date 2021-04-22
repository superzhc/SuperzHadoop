package com.github.superzhc.flink.manage.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.superzhc.flink.manage.entity.JobInfo;
import com.github.superzhc.flink.manage.job.monitor.JobMonitor;
import com.github.superzhc.flink.manage.job.monitor.JobMonitorConstant;
import com.github.superzhc.flink.manage.job.monitor.JobMonitorFactory;
import com.github.superzhc.flink.manage.job.monitor.entity.JobMonitorInfo;
import com.github.superzhc.flink.manage.job.monitor.entity.YarnJobMonitorInfo;
import com.github.superzhc.flink.manage.service.IJobInfoService;
import com.github.superzhc.flink.manage.util.FrontListParams;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        QueryWrapper<JobInfo> queryWrapper = new QueryWrapper<>();
        String jobName = params.getParam("jobName");
        if (StrUtil.isNotBlank(jobName)) {
            queryWrapper.like("job_name", jobName);
        }
        String jobStatus = params.getParam("jobStatus");
        if (StrUtil.isNotBlank(jobStatus)) {
            queryWrapper.eq("job_status", jobStatus);
        }
        IPage<JobInfo> lst = jobInfoService.page(params.page(), params.orderBy(queryWrapper));
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

    @GetMapping("/statistics")
    public Result jobStatistics() {
        Map<String, Integer> map = jobInfoService.numByStatus();
        return Result.success(map);
    }

    @PostMapping("/fetch/yarn/{applicationId}")
    public Result fetchYarnApplication(@PathVariable String applicationId) {
        boolean exist = jobInfoService.exist(applicationId);
        if (exist) {
            return Result.fail("{}已存在，无需获取", applicationId);
        }

        JobMonitorInfo jobMonitorInfo = jobMonitorFactory.getYarnJobMonitor().info(applicationId);
        if (null == jobMonitorInfo) {
            return Result.fail("获取失败，接口返回数据为空");
        }

        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobApplicationId(jobMonitorInfo.getId());
        jobInfo.setJobName(jobMonitorInfo.getName());
        jobInfo.setJobMode(jobMonitorInfo.getMode());
        jobInfo.setJobStatus(jobMonitorInfo.getState());
        jobInfo.setJobType(jobMonitorInfo.getType());
        jobInfo.setWebui(jobMonitorInfo.getWebui());
        boolean b = jobInfoService.save(jobInfo);
        return b ? Result.success("获取成功") : Result.fail("获取失败");
    }

    @PostMapping("/update")
    public Result update(int id) {
        JobInfo jobInfo = jobInfoService.getById(id);
        if (null == jobInfo) {
            return Result.fail("未查到任务：[id={}]", id);
        } else if (StrUtil.isBlank(jobInfo.getJobApplicationId())) {
            return Result.fail("尚未设置任务的ApplicationId");
        }

        JobMonitor jobMonitor = jobMonitorFactory.getJobMonitor(jobInfo.getJobMode());
        JobMonitorInfo jobMonitorInfo = jobMonitor.info(jobInfo.getJobApplicationId());
        if (null == jobMonitorInfo) {
            return Result.fail("更新失败，接口返回数据为空");
        }

        jobInfo.setJobName(jobMonitorInfo.getName());
        jobInfo.setJobStatus(jobMonitorInfo.getState());
        jobInfo.setWebui(jobMonitorInfo.getWebui());
        boolean b = jobInfoService.updateById(jobInfo);
        return b ? Result.success("更新成功") : Result.fail("更新失败");
    }
}

