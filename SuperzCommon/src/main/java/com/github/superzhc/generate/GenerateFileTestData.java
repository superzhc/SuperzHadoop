package com.github.superzhc.generate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

/**
 * 2020年11月03日 superz add
 */
public abstract class GenerateFileTestData implements GenerateTestData
{
    public String target() {
        String s = path();
        if (!s.endsWith(File.separator))
            s += File.separator;
        return s;
    }

    public abstract String path();

    public int partitions() {
        return 1;
    }

    /**
     * 文件后缀名
     * @return
     */
    public abstract String suffix();

    public String filename(int partition) {
        String name = String.format("part-r-%05d", partition);
        String suf = (null != suffix() && suffix().length() > 0 && !suffix().startsWith("\\.")) ? "." + suffix() : "";
        return name + suf;
    }

    @Override
    public void generate(Supplier<String> supplier, long quantity) {
        // 获取分区数
        int num = partitions();
        // 计算步长
        long stride = quantity / num;

        try {
            // 目录操作
            File dir = new File(target());
            if (!dir.exists()) {// 如果目录不存在，直接创建目录
                dir.mkdirs();
            }

            for (int i = 0; i < num; i++) {
                // 每个文件的名称
                String fullName = target() + filename(i);
                // 每个文件生成数据的数量
                long partition_quantity = stride;
                if (num == i + 1) {
                    partition_quantity = quantity - i * stride;
                }
                File f = new File(fullName);
                OutputStream out = new FileOutputStream(f, false);
                byte[] content = generateContent(supplier, partition_quantity);
                out.write(content);
                out.close();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract byte[] generateContent(Supplier<String> supplier, long quantity);
}
