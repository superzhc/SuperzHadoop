package com.github.superzhc.xxljob.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 2020年11月23日 superz add
 */
public class BeanUtils {
    public static <T> T mapToBean(Map<String, ?> map, Class<T> beanClass) {
        if (null == map) {
            return null;
        }

        try {
            boolean emptyConstructor = false;
            Constructor[] constructors = beanClass.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    emptyConstructor = true;
                    break;
                }
            }
            if (!emptyConstructor) {
                throw new RuntimeException("无空构造函数，Map无法转" + beanClass.getName());
            }

            T obj = beanClass.newInstance();

            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                // 静态变量不做处理，一般Bean中不存在静态变量
                if (Modifier.isStatic(mod)) {
                    continue;
                }

                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }

            return obj;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> beanToMap(Object obj) {
        return beanToMap(obj, true);
    }

    /**
     * 实体转Map
     * @param obj
     * @param isnull 是否将null值的字段加入map中
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj, boolean isnull) {
        if (null == obj) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                // 静态变量不做处理，一般Bean中不存在静态变量
                if (Modifier.isStatic(mod)) {
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(obj);
                if (null == value) {
                    if (isnull) {
                        map.put(field.getName(), value);
                    }
                } else {
                    map.put(field.getName(), value);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

        return map;
    }
}
