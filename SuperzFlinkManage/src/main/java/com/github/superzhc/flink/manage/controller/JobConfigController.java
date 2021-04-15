package com.github.superzhc.flink.manage.controller;

import cn.hutool.core.collection.CollUtil;
import com.github.superzhc.flink.manage.entity.JobConfig;
import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;
import com.github.superzhc.flink.manage.job.builder.JobBuilder;
import com.github.superzhc.flink.manage.job.executor.JobExecutor;
import com.github.superzhc.flink.manage.service.IJobConfigService;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务配置控制器
 * </p>
 *
 * @author superz
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/job-config")
public class JobConfigController {

    @Autowired
    private IJobConfigService jobConfigService;

    @Autowired
    private JobBuilder jobBuilder;

    @Autowired
    private JobExecutor jobExecutor;

    @GetMapping
    public Result<List<JobConfig>> get() {
        List<JobConfig> jobConfigs = jobConfigService.list();
        return Result.success(jobConfigs);
    }

    @GetMapping("/{id}")
    public Result<JobConfig> get(@PathVariable("id") int id) {
        JobConfig jobConfig = jobConfigService.getById(id);
        return Result.success(jobConfig);
    }

    @PostMapping
    public Result add(JobConfig jobConfig) {
        boolean b = jobConfigService.save(jobConfig);
        return b ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping
    public Result edit(JobConfig jobConfig) {
        boolean b = jobConfigService.updateById(jobConfig);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") int id) {
        boolean b = jobConfigService.removeById(id);
        return b ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @PostMapping("/command")
    public Result command(int id) {
        JobConfigVO jobConfigVO = jobConfigService.getJobConfig(id);
        if (null == jobConfigVO) {
            return Result.fail("任务配置Id为[{}]不存在", id);
        }

        List<String> command = jobBuilder.build(jobConfigVO);
        return Result.success(CollUtil.join(command, " "));
    }

    @PostMapping("/run")
    public Result run(int id) {
        JobConfigVO jobConfigVO = jobConfigService.getJobConfig(id);
        if (null == jobConfigVO) {
            return Result.fail("任务配置Id为[{}]不存在", id);
        }

        List<String> command = jobBuilder.build(jobConfigVO);
        jobExecutor.uploadJar(jobConfigVO.getJobPackagePath());
        String result = jobExecutor.execute(command);
        return Result.success(result);
    }
}
