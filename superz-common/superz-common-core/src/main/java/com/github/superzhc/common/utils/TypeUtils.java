package com.github.superzhc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/2/23 11:00
 **/
public class TypeUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TypeUtils.class);

    private TypeUtils() {
    }

    public static boolean isPrimitive(Object obj) {
        return null == obj ? false : obj.getClass().isPrimitive();
    }

    public static boolean isArray(Object obj) {
        return null == obj ? false : obj.getClass().isArray();
    }

    public static boolean isEnum(Object obj) {
        return null == obj ? false : obj.getClass().isEnum();
    }

    public static boolean isInt(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj.getClass().isPrimitive()) {
            return obj.getClass() == Integer.TYPE;
        } else {
            return obj.getClass() == Integer.class;
        }
    }

    public static boolean isLong(Object obj){
        if (null == obj) {
            return false;
        }

        if (obj.getClass().isPrimitive()) {
            return obj.getClass() == Long.TYPE;
        } else {
            return obj.getClass() == Long.class;
        }
    }

    public static boolean isFloat(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj.getClass().isPrimitive()) {
            return obj.getClass() == Float.TYPE;
        } else {
            return obj.getClass() == Float.class;
        }
    }

    public static boolean isDouble(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj.getClass().isPrimitive()) {
            return obj.getClass() == Double.TYPE;
        } else {
            return obj.getClass() == Double.class;
        }
    }

    public static boolean isBool(Object obj){
        if (null == obj) {
            return false;
        }

        if (obj.getClass().isPrimitive()) {
            return obj.getClass() == Boolean.TYPE;
        } else {
            return obj.getClass() == boolean.class;
        }
    }
}
