package com.github.superzhc.flink.manage.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Date;

/**
 * @author superz
 * @create 2021/4/9 15:54
 */
@Data
@Configuration
@ConfigurationProperties("jar.packages")
@PropertySource("classpath:bigdata.properties")
public class JarPackagesConfig {
    private String mode;
    private String root;
    private String path;
    private String temp;
    private String upload;

    /**
     * 临时文件的保存路径为 ${root}/${temp_path}/${date}/${uuid}/${fileName}
     * 示例：D://data/temp/2021-04-14/xxxxxxxxxxxxxxxxxxxxxxxxx/test.jar
     * 添加一层时间目录，可以根据时间来定期删除以前的临时文件
     * 添加一层UUID目录，保证临时文件的唯一性，保证临时文件不会因为名称相同被覆盖掉
     */
    public String fullTempPath(String fileName) {
        return StrUtil.format("{}{}/{}/{}/{}", root, temp, DateUtil.formatDate(new Date()), StrUtil.uuid(), fileName);
    }

    /**
     * 正式文件的保存路径为 ${root}/${path}/${packageName}/${version}/${fileName}
     * 示例：D://data/custom/test-package/1.0/test.jar
     */
    public String fullPath(String filePath) {
        return StrUtil.format("{}{}/{}", root, path, filePath);
    }

    /**
     * 上传算子包的上传后地址
     * 上传地址的路径为 ${upload}/${packageName}/${version}/${fileName}
     */
    public String uploadPath(String filePath) {
        return StrUtil.format("{}/{}", upload, filePath);
    }
}
