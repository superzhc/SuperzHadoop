package com.github.superzhc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/15 10:22
 **/
public class SystemUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUtils.class);

    public static void setEnv(String key, String value) {
        LOG.info("设置环境变量:[{}={}]", key, value);
        try {
            // jdk1.8 读全部系统环境变量的字段和读单个环境变量的字段不是同一个
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.put(key, value);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.put(key, value);
        } catch (Exception e) {
            try {
                Map<String, String> env = System.getenv();
                Class<?> cl = env.getClass();
                Field field = cl.getDeclaredField("m");
                field.setAccessible(true);
                Map<String, String> writableEnv = (Map<String, String>) field.get(env);
                writableEnv.put(key, value);
            } catch (Exception innerE) {
                LOG.error("设置环境变量:[{}={}]失败", key, value);
                throw new IllegalStateException("Failed to set environment variable", innerE);
            }
        }
    }
}
