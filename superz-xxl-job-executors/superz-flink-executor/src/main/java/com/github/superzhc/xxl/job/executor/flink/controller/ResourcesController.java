package com.github.superzhc.xxl.job.executor.flink.controller;

import com.github.superzhc.xxl.job.executor.flink.common.ResultT;
import com.github.superzhc.xxl.job.executor.flink.dto.ResourcesDTO;
import com.github.superzhc.xxl.job.executor.flink.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 磁盘文件目录控制器
 *
 * @author superz
 * @create 2021/9/16 15:03
 */
@Slf4j
@RestController
@RequestMapping("/resources")
public class ResourcesController {
    @PostMapping
    public ResultT list(@RequestBody(required = false) ResourcesDTO dto) {
        String path = System.getProperty("user.home");
        if (null != dto && null != dto.getPath() && !"".equals(dto.getPath().trim())) {
            path = dto.getPath();
        }

        File parent = new File(path);
        if (!parent.exists()) {
            return ResultT.fail("路径[{0}]不存在", path);
        }

        /*String[] children = file.list();*/
        File[] children = parent.listFiles();
        List<Map<String, Object>> dirResult = new ArrayList<>();
        List<Map<String, Object>> fileResult = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (File child : children) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", child.getName());
            map.put("isdir", child.isDirectory());

            StringBuilder auth = new StringBuilder();
            auth.append(child.canRead() ? 'r' : '-').append(child.canWrite() ? 'w' : '-').append(child.canExecute() ? 'x' : '-');
            map.put("auth", auth.toString());

            map.put("lastmodified", sdf.format(child.lastModified()));
            map.put("path", child.getAbsolutePath());

            if (child.isDirectory()) {
                dirResult.add(map);
            } else {
                map.put("length", human(child.length()));
                fileResult.add(map);
            }
        }

        // 合并文件列表
        dirResult.addAll(fileResult);
        return ResultT.success(dirResult);
    }

    @PostMapping("/rename")
    public ResultT rename(@RequestBody ResourcesDTO dto) {
        File file = new File(dto.getPath());
        if (!file.exists()) {
            return ResultT.msg(ResultT.DEFAULT_FAIL_CODE, "目录/文件[{0}]不存在", dto.getPath());
        }

        String rootDir = dto.getPath().substring(0, dto.getPath().replace("\\", File.separator).replace("/", File.separator).lastIndexOf(File.separator) + 1);
        File newFile = new File(rootDir, dto.getName());
        if (newFile.exists()) {
            return ResultT.msg(ResultT.DEFAULT_FAIL_CODE, "目录/文件[{0}]已存在", dto.getPath());
        }
        file.renameTo(newFile);
        return ResultT.msg(ResultT.DEFAULT_SUCCESS_CODE, "重命名成功");
    }

    @PostMapping("/delete")
    public ResultT delete(@RequestBody ResourcesDTO dto) {
        File file = new File(dto.getPath());
        if (!file.exists()) {
            return ResultT.fail("目录/文件[{0}]不存在", dto.getPath());
        }

        boolean b = file.delete();
        if (!b) {
            return ResultT.msg(ResultT.DEFAULT_FAIL_CODE, "删除失败");
        } else {
            return ResultT.msg(ResultT.DEFAULT_SUCCESS_CODE, "删除成功");
        }
    }

    @PostMapping("/upload")
    public ResultT upload(@RequestParam("path") String path, @RequestParam("file") MultipartFile file) {
        if (null == file) {
            return ResultT.fail("请选择上传的文件");
        }
        path = path.endsWith(File.separator) ? path : path + File.separator;
        String fileName = file.getOriginalFilename();
        File fileObj = new File(path, fileName);
        if (fileObj.exists()) {
            return ResultT.fail("文件[{0}]已存在", fileName);
        }
        try {
            file.transferTo(fileObj);
            return ResultT.msg(ResultT.DEFAULT_FAIL_CODE, "上传成功");
        } catch (IOException e) {
            return ResultT.fail(e);
        }
    }

    @PostMapping("/multiupload")
    public ResultT multiupload(@RequestParam("path") String path, @RequestParam("files") MultipartFile files[]) {
        if (null == files || files.length == 0) {
            return ResultT.fail("请选择上传的文件");
        }

        path = path.endsWith(File.separator) ? path : path + File.separator;
        for (int i = 0, len = files.length; i < len; i++) {
            String fileName = files[i].getOriginalFilename();
            File file = new File(path, fileName);
            if (file.exists()) {
                return ResultT.fail("文件[{0}]已存在", fileName);
            }
            try {
                files[i].transferTo(file);
            } catch (IOException e) {
                return ResultT.fail(e);
            }
        }
        return ResultT.msg(ResultT.DEFAULT_FAIL_CODE, "上传成功");
    }

    @GetMapping("/download")
    public ResultT download(HttpServletResponse response, @RequestParam String path) {
        File file = new File(path);
        if (!file.exists()) {
            return ResultT.fail("文件[{0}]不存在", path);
        }

        if (file.isDirectory()) {
            String fileName = file.getName() + ".zip";
            response.reset();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            try {
                ZipUtils.toZip(response.getOutputStream(), file);
                return ResultT.msg(ResultT.DEFAULT_SUCCESS_CODE, "下载成功");
            } catch (IOException e) {
                return ResultT.fail(e);
            }
        } else {
            String fileName = path.substring(path.replace("\\", File.separator).replace("/", File.separator).lastIndexOf(File.separator) + 1);
            response.reset();
            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
                return ResultT.msg(ResultT.DEFAULT_SUCCESS_CODE, "下载成功");
            } catch (IOException e) {
                return ResultT.fail(e);
            }
        }
    }

    @GetMapping("/multidownload")
    public ResultT multiDownload(HttpServletResponse response, @RequestParam String path, @RequestParam String files) {
        path = path.endsWith(File.separator) ? path : path + File.separator;
        String[] fileNames = files.split(",");
        for (int i = 0, len = fileNames.length; i < len; i++) {
            fileNames[i] = path + fileNames[i];
        }
        String zipName = "Resources_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
        response.reset();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        try {
            ZipUtils.toZip(response.getOutputStream(), fileNames);
            return ResultT.msg(ResultT.DEFAULT_SUCCESS_CODE, "下载成功");
        } catch (IOException e) {
            return ResultT.fail(e);
        }
    }

    private String human(Long size) {
        String sf = "%.2f%s";
        if (size < 1024) {
            return String.format(sf, size.doubleValue(), "B");
        }
        Double d = size / 1024.0;
        if (d < 1024.0) {
            return String.format(sf, d, "KB");
        }
        d = size / 1024.0 / 1024.0;
        if (d < 1024.0) {
            return String.format(sf, d, "MB");
        }
        d = size / 1024.0 / 1024.0 / 1024.0;
        if (d < 1024.0) {
            return String.format(sf, d, "GB");
        }
        return "";
    }
}
