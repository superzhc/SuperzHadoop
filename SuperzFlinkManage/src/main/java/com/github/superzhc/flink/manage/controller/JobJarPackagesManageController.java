package com.github.superzhc.flink.manage.controller;

import cn.hutool.core.io.FileUtil;
import com.github.superzhc.flink.manage.entity.JobJarPackagesManage;
import com.github.superzhc.flink.manage.job.packages.JobPackages;
import com.github.superzhc.flink.manage.service.IJobJarPackagesManageService;
import com.github.superzhc.flink.manage.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 任务Jar包管理
 *
 * @author superz
 * @create 2021/4/9 15:25
 */
@Slf4j
@RestController
@RequestMapping("/job-jar-packages-manage")
public class JobJarPackagesManageController {
    @Autowired
    private JobPackages jobPackages;

    @Autowired
    private IJobJarPackagesManageService jobJarPackagesManageService;

    @GetMapping
    public Result<List<JobJarPackagesManage>> list() {
        List<JobJarPackagesManage> lst = jobJarPackagesManageService.list();
        return Result.success(lst);
    }

    @GetMapping("/{id}")
    public Result<JobJarPackagesManage> get(@PathVariable("id") int id) {
        JobJarPackagesManage jobJarPackagesManage = jobJarPackagesManageService.getById(id);
        return Result.success(jobJarPackagesManage);
    }

    @PostMapping
    public Result add(JobJarPackagesManage jobJarPackagesManage) {
        // 判断包是否已经存在
        if (jobJarPackagesManageService.exist(jobJarPackagesManage.getPackageName(),
                jobJarPackagesManage.getVersion())) {
            return Result.fail("版本为{}的{}包已经存在", jobJarPackagesManage.getVersion(),
                    jobJarPackagesManage.getPackageName());
        }

        try {
            // 将上传的包从临时目录转移到正式目录下
            String targetPath = jobPackages.transferTo(jobJarPackagesManage.getPackagePath(), jobJarPackagesManage.getPackageName(), jobJarPackagesManage.getVersion(), FileUtil.getName(jobJarPackagesManage.getPackagePath()));
            jobJarPackagesManage.setPackagePath(targetPath);
        } catch (Exception e) {
            return new Result(e);
        }

        boolean b = jobJarPackagesManageService.save(jobJarPackagesManage);
        return b ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping
    public Result edit(JobJarPackagesManage jobJarPackagesManage) {
        try {
            JobJarPackagesManage jobJarPackagesManageDB =
                    jobJarPackagesManageService.getById(jobJarPackagesManage.getId());
            if (null == jobJarPackagesManageDB) {
                return Result.fail("算子包不存在");
            }

            // 将包转移到修改后的指定路径下
            String targetPath = jobPackages.transferTo(jobJarPackagesManageDB.getPackagePath(), jobJarPackagesManage.getPackageName(), jobJarPackagesManage.getVersion(), FileUtil.getName(jobJarPackagesManageDB.getPackagePath()));
            jobJarPackagesManage.setPackagePath(targetPath);
        } catch (Exception e) {
            return new Result(e);
        }
        boolean b = jobJarPackagesManageService.updateById(jobJarPackagesManage);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") int id) {
        JobJarPackagesManage jobJarPackagesManage = jobJarPackagesManageService.getById(id);
        String packagePath = jobJarPackagesManage.getPackagePath();
        boolean b = jobJarPackagesManageService.removeById(id);
        if (b) {
            //同时删除文件
            boolean b1 = jobPackages.delete(packagePath);
            return b1 ? Result.success("删除成功") : Result.fail("删除失败");
        }
        return Result.fail("删除失败");
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("上传文件不能为空");
        }

        try {
            String path = jobPackages.saveTemp(file.getOriginalFilename(), file.getInputStream());
            return Result.<String>success(path);
        } catch (Exception e) {
            return Result.fail(e);
        }
    }
}
