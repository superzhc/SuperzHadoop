package com.github.superzhc.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 *
 * @author superz
 * @create 2021/4/8 17:13
 */
public class ReflectionUtils {

    /**
     * 获取所有字段（public、protected和private），也包含父类字段
     * @param clazz
     * @return
     */
    public static Field[] getDeclaredFields(Class<?> clazz) {
        List<Field> lst = new ArrayList<>();

        Class cls = clazz;
        // 当父类为Object的时候说明到达了最上层
        while (cls != Object.class) {
            Field[] fs = cls.getDeclaredFields();
            if (null != fs && fs.length > 0) {
                lst.addAll(Arrays.asList(fs));
            }
            cls = cls.getSuperclass();
        }

        Field[] fields = new Field[lst.size()];
        for (int i = 0, len = lst.size(); i < len; i++) {
            fields[i] = lst.get(i);
        }
        return fields;
    }
}
