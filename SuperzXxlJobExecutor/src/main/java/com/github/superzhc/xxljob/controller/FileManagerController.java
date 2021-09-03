package com.github.superzhc.xxljob.controller;

import com.github.superzhc.xxljob.common.ResultT;
import com.github.superzhc.xxljob.config.BigdataConfig;
import com.github.superzhc.xxljob.entity.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author superz
 * @create 2021/7/20 19:49
 */
@RestController
@RequestMapping("/bigdata")
public class FileManagerController {

    @Autowired
    private BigdataConfig bigdataConfig;

    @GetMapping("/lib/item")
    public ResultT libs() {
        return ResultT.success(bigdataConfig.getLib());
    }

    @GetMapping("/lib")
    public ResultT runtimePackage(@RequestParam String uid) {
        String strPath = bigdataConfig.getLib().get(uid);
        File file = new File(strPath);
        if (!file.exists()) {
            return ResultT.fail("文件/目录不存在,{0}", file.toPath().toAbsolutePath());
        }

        List<FileInfo> lst = new ArrayList<>();
        try {
            File[] childrenFile = file.listFiles();
            for (File child : childrenFile) {
                //Path childPath = child.toPath();

                // 不显示文件夹
                if (!child.isFile()) {
                    continue;
                }

                FileInfo fi = new FileInfo();
                fi.setName(child.getName());

                lst.add(fi);
            }
        } catch (Exception ex) {
            return ResultT.fail(ex.toString());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("count", lst.size());
        data.put("data", lst);
        return ResultT.success(data);
    }

    @PostMapping("/upload")
    public ResultT upload(@RequestParam String uid, @RequestParam("file") MultipartFile file) {
        String strPath = bigdataConfig.getLib().get(uid);

        if (file.isEmpty()) {
            return ResultT.fail("请选择上传文件");
        }

        String fileName = file.getOriginalFilename();
        File dest = new File(strPath + fileName);
        try {
            file.transferTo(dest);
            return ResultT.success("上传成功");
        } catch (IOException e) {
            return ResultT.fail(e.toString());
        }
    }
}
