package com.github.superzhc.flink.manage.job.packages;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author superz
 * @create 2021/4/14 14:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPackagesPath {
    private String packageName;
    private String version;
    private String fileName;

    public static JobPackagesPath parse(String fullPath) {
        // 使用FileUtil#getAbsolutePath保证路径分隔符统一为 /
        String[] ss = FileUtil.getAbsolutePath(fullPath).split("/");
        int len = ss.length;
        if (len < 3) {
            throw new RuntimeException("算子包地址必须包含：包名/版本/jar包，请检查路径");
        }

        return new JobPackagesPath(ss[len - 3], ss[len - 2], ss[len - 1]);
    }

    public String path() {
        return StrUtil.format("{}/{}/{}", packageName, version, fileName);
    }
}
