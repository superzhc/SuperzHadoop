package com.github.superzhc.common.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author superz
 * @create 2023/5/12 10:14
 **/
public class MethodUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MethodUtils.class);

    private MethodUtils() {
    }

    public static <T> T invoke(Object instance, String methodName, Object... params) {
        if (null == instance) {
            return null;
        }

        try {
            Class clazz = TypeUtils.getClass(instance);
            Class[] paramterTypes = TypeUtils.getClasses(params);
            Method method = getDeclaredMethod(clazz, methodName, paramterTypes);
            return (T) method.invoke(instance, params);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (null == clazz) {
            throw new IllegalArgumentException("Class object is not null");
        }

        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

//    public static Type returnType(Method method) {
//        Type genericReturnType = method.getGenericReturnType();
//        if (genericReturnType instanceof ParameterizedType) {
//            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
//
//            // 获取参数类型
//            Type rawType = parameterizedType.getRawType();
//
//            // 获取参数的泛型列表
//            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//            return actualTypeArguments[0];
//        } else {
//            return genericReturnType;
//        }
//    }
//
//    public static Type[] parameterTypes(Method method) {
//        Type[] genericParameterTypes = method.getGenericParameterTypes();
//        for (Type genericParameterType : genericParameterTypes) {
//
//        }
//        return genericParameterTypes;
//    }

}
