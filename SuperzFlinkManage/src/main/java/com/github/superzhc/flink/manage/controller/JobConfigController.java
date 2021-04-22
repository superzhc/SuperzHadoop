package com.github.superzhc.flink.manage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.superzhc.flink.manage.entity.JobConfig;
import com.github.superzhc.flink.manage.entity.JobJarPackagesManage;
import com.github.superzhc.flink.manage.entity.vo.JobConfigVO;
import com.github.superzhc.flink.manage.job.builder.JobBuilder;
import com.github.superzhc.flink.manage.job.executor.JobExecutor;
import com.github.superzhc.flink.manage.service.IJobConfigService;
import com.github.superzhc.flink.manage.service.IJobJarPackagesManageService;
import com.github.superzhc.flink.manage.util.FrontListParams;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IJobJarPackagesManageService jobJarPackagesManageService;

    @Autowired
    private JobBuilder jobBuilder;

    @Autowired
    private JobExecutor jobExecutor;

    @GetMapping
    public Result<IPage<JobConfig>> list(FrontListParams params) {
        QueryWrapper queryWrapper = new QueryWrapper();
        String jobName = params.getParam("jobName");
        if (StrUtil.isNotBlank(jobName)) {
            queryWrapper.like("job_name", jobName);
        }
        IPage<JobConfig> jobConfigs = jobConfigService.page(params.page(), params.orderBy(queryWrapper));
        return Result.success(jobConfigs);
    }

    @GetMapping("/{id}")
    public Result<JobConfig> get(@PathVariable("id") int id) {
        JobConfig jobConfig = jobConfigService.getById(id);
        //包相关信息
        JobJarPackagesManage jobJarPackagesManage = jobJarPackagesManageService.getById(jobConfig.getJobJarPackage());
        if (null != jobJarPackagesManage) {
            Map<String, Object> packageInfo = new HashMap<>();
            packageInfo.put("packageName", jobJarPackagesManage.getPackageName());
            packageInfo.put("version", jobJarPackagesManage.getVersion());
            return Result.success(jobConfig, packageInfo);
        } else {
            return Result.success(jobConfig);
        }
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

    @PostMapping("/batch-delete")
    public Result batchDelete(@RequestParam(value = "ids[]") Integer[] ids) {
        for (Integer id : ids) {
            boolean b = jobConfigService.removeById(id);
            if (!b) {
                return Result.fail("删除失败");
            }
        }
        return Result.success("删除成功");
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
