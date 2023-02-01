package com.github.superzhc.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    public static <T> T instance(String className, Object... params) {
        try {
            Class<?> clazz = Class.forName(className);
            return instance(clazz, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 类实例化，支持参数构造函数
     *
     * @param clazz
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T instance(Class<?> clazz, Object... params) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(paramsType(params));
            constructor.setAccessible(true);
            return (T) constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用静态方法
     *
     * @param clazz
     * @param methodName
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T invokeStaticMethod(Class<?> clazz, String methodName, Object... params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramsType(params));
            method.setAccessible(true);
            return (T) method.invoke(null, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用方法
     *
     * @param clazz
     * @param methodName
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T invokeMethod(Class<?> clazz, String methodName, Object... params) {
        Object obj = instance(clazz);
        return invokeMethod(obj, methodName, params);
    }

    /**
     * 调用方法
     *
     * @param clazz
     * @param constructorArgs
     * @param methodName
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T invokeMethod(Class<?> clazz, Object[] constructorArgs, String methodName, Object... params) {
        Object obj = instance(clazz, constructorArgs);
        return invokeMethod(obj, methodName, params);
    }

    /**
     * 调用方法
     *
     * @param instance
     * @param methodName
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T invokeMethod(Object instance, String methodName, Object... params) {
        Class<?> clazz = instance.getClass();

        try {
            Method method = clazz.getDeclaredMethod(methodName, paramsType(params));
            method.setAccessible(true);
            return (T) method.invoke(instance, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Class<T> methodReturnType(Class<?> clazz, String methodName, Object... params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramsType(params));
            return (Class<T>) method.getReturnType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有参数类型
     *
     * @param params
     *
     * @return
     */
    private static Class<?>[] paramsType(Object... params) {
        if (null == params || params.length == 0) {
            return null;
        }

        int len = params.length;
        Class<?>[] paramsClass = new Class[len];
        for (int i = 0; i < len; i++) {
            paramsClass[i] = params[i].getClass();
        }
        return paramsClass;
    }

    /**
     * 获取所有字段（public、protected和private），也包含父类字段
     *
     * @param clazz
     *
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
