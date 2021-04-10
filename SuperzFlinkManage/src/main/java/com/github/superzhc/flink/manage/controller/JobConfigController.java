package com.github.superzhc.flink.manage.controller;

import java.util.List;

import com.github.superzhc.flink.manage.config.FlinkConfig;
import com.github.superzhc.flink.manage.entity.JobConfig;
import com.github.superzhc.flink.manage.entity.JobJarPackagesManage;
import com.github.superzhc.flink.manage.model.run.FlinkRunDefaultModeOptions;
import com.github.superzhc.flink.manage.model.run.FlinkRunYarnClusterModeOptions;
import com.github.superzhc.flink.manage.parse.FlinkRunCLIParse;
import com.github.superzhc.flink.manage.service.IJobConfigService;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.superzhc.flink.manage.model.run.FlinkRunCLI;
import com.github.superzhc.flink.manage.service.IJobJarPackagesManageService;

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
    private FlinkConfig flinkConfig;

    @Autowired
    private IJobConfigService jobConfigService;

    @Autowired
    private IJobJarPackagesManageService jobJarPackagesManageService;

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

    @PostMapping("/run")
    public Result run(int id) {
        JobConfig jobConfig = jobConfigService.getById(id);
        if (null == jobConfig) {
            return Result.fail("任务Id为[{0}]不存在", id);
        }

        JobJarPackagesManage jobJarPackagesManage = jobJarPackagesManageService.getById(jobConfig.getJobJarPackage());
        if (null == jobJarPackagesManage) {
            return Result.fail("任务Id为[{0}]对应的Jar包Id为[{1}]不存在", id, jobConfig.getJobJarPackage());
        }

        // TODO:运行逻辑
        FlinkRunCLI flinkRunCLI = new FlinkRunCLI();
        flinkRunCLI.setFlink(flinkConfig.flinkShell());
        // 目前是本地模式，考虑做一步下载jar包的操作
        flinkRunCLI.setJarFile(jobJarPackagesManage.getPackagePath());
        flinkRunCLI.setArguments(jobConfig.getJobArguments());
        // Yarn-Cluster 模式
        if ("yarn-cluster".equals(jobConfig.getJobMode())) {
            FlinkRunYarnClusterModeOptions options=new FlinkRunYarnClusterModeOptions();
            options.setClassname(jobConfig.getJobMainClass());

            flinkRunCLI.setOptions(options);
        } else {
            // flink run <jar-file>
            FlinkRunDefaultModeOptions options=new FlinkRunDefaultModeOptions();
            options.setClassname(jobConfig.getJobMainClass());

            flinkRunCLI.setOptions(options);
        }
        List<String> command=new FlinkRunCLIParse(flinkRunCLI).parse();
        return Result.success(command);
    }
}
