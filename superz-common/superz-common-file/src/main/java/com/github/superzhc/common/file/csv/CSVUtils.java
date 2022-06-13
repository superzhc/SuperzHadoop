package com.github.superzhc.common.file.csv;

import com.github.superzhc.common.file.text.TextWriter;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * @author superz
 * @create 2022/3/9 17:22
 */
public final class CSVUtils {
    /**
     * 考虑还是直接使用 TextUtils
     *
     * @param path
     * @param datas
     * @return
     */
    @Deprecated
    public static boolean write(String path, Object[][] datas) {
        StringBuilder sb = new StringBuilder();
        for (Object[] data : datas) {
            String str = Arrays.stream(data).map(d -> null == data ? (String) null : data.toString()).collect(Collectors.joining(","));
            sb.append(str).append("\n");
        }
        return TextWriter.write(path, sb.toString());
    }
}
