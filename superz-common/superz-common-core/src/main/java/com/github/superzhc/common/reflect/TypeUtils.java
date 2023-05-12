package com.github.superzhc.common.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/5/12 9:55
 **/
public class TypeUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TypeUtils.class);

    private TypeUtils() {
    }

    /**
     * {@code null}安全的获取对象类型
     *
     * @param <T> 对象类型
     * @param obj 对象，如果为{@code null} 返回{@code null}
     * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T obj) {
        return ((null == obj) ? null : (Class<T>) obj.getClass());
    }

    public static Class[] getClasses(Object... params) {
        if (null == params || params.length == 0) {
            return null;
        }

        Class[] classes = new Class[params.length];
        for (int i = 0, len = params.length; i < len; i++) {
            classes[i] = getClass(params[i]);
        }
        return classes;
    }

    /**
     * 获得外围类<br>
     * 返回定义此类或匿名类所在的类，如果类本身是在包中定义的，返回{@code null}
     *
     * @param clazz 类
     * @return 外围类
     */
    public static Class<?> getEnclosingClass(Class<?> clazz) {
        return null == clazz ? null : clazz.getEnclosingClass();
    }
}
