package com.github.superzhc.flink.manage.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import com.github.superzhc.flink.manage.config.JarPackagesConfig;
import com.github.superzhc.flink.manage.entity.JobJarPackagesManage;
import com.github.superzhc.flink.manage.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.superzhc.flink.manage.service.IJobJarPackagesManageService;

import lombok.extern.slf4j.Slf4j;

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
    private JarPackagesConfig jarPackagesConfig;

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
            return Result.fail("版本为{0}的{1}包已经存在", jobJarPackagesManage.getVersion(),
                jobJarPackagesManage.getPackageName());
        }

        try {
            // 将上传的包从临时目录转移到正式目录下
            File tmpFile = new File(jobJarPackagesManage.getPackagePath());
            String targetPath = jarPackagesConfig.getRoot() + "custom/" + jobJarPackagesManage.getPackageName() + "/"
                + jobJarPackagesManage.getVersion() + "/" + tmpFile.getName();
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            } else if (targetFile.exists()) {
                targetFile.delete();
            }
            Files.copy(tmpFile.toPath(), targetFile.toPath());
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
                return Result.fail("Jar包不存在");
            }
            // 将包转移到修改后的指定路径下
            File tmpFile = new File(jobJarPackagesManageDB.getPackagePath());
            String targetPath = jarPackagesConfig.getRoot() + "custom/" + jobJarPackagesManage.getPackageName() + "/"
                + jobJarPackagesManage.getVersion() + "/" + tmpFile.getName();
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            } else if (targetFile.exists()) {
                targetFile.delete();
            }
            Files.copy(tmpFile.toPath(), targetFile.toPath());
            jobJarPackagesManage.setPackagePath(targetPath);
        } catch (Exception e) {
            return new Result(e);
        }
        boolean b = jobJarPackagesManageService.updateById(jobJarPackagesManage);
        return b ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") int id) {
        boolean b = jobJarPackagesManageService.removeById(id);
        return b ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        try {
            String destPath = jarPackagesConfig.getRoot() + "tmp/" + UUID.randomUUID().toString() + "/" + fileName;
            File dest = new File(destPath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return Result.success(destPath);
        } catch (Exception e) {
            return new Result(e);
        }
    }
}
