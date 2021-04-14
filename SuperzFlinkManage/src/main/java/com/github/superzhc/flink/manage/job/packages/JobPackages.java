package com.github.superzhc.flink.manage.job.packages;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.InputStream;

/**
 * 任务的包管理
 *
 * @author superz
 * @create 2021/4/13 19:49
 */
public interface JobPackages {
    /**
     * 保存任务包到临时文件夹下，返回保存的路径
     *
     * @param content
     * @return
     */
    String saveTemp(String fileName, InputStream content);

    /**
     * 保存任务包
     *
     * @param packageName
     * @param version
     * @param fileName
     * @param content
     * @return
     */
    String save(String packageName, String version, String fileName, InputStream content);

    /**
     * 获取任务包的流
     * @param path
     * @return
     */
    InputStream get(String path);

    /**
     * 将任务包转移到新的目录下，返回任务包所在的新地址
     *
     * @param oldPath
     * @param packageName
     * @param version
     * @param fileName
     * @return
     */
    default String transferTo(String oldPath, String packageName, String version, String fileName) {
        InputStream input = get(oldPath);
        String newPath = save(packageName, version, fileName, input);
        return newPath;
    }

    default String downloadLocal(String source,String target){
        InputStream input=get(source);
        File file = FileUtil.writeFromStream(input, target);
        return file.getPath();
    }

    /**
     * 删除任务包
     * @param path
     * @return
     */
    boolean delete(String path);
}
