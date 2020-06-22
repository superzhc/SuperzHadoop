package com.github.superzhc.util;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2020年06月22日 superz add
 */
public class PropertiesUtils
{
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Map<String, Properties> fileProperties = new ConcurrentHashMap<>();
    public static Map<String, String> properties = new ConcurrentHashMap<>();

    public static void read(String path) {
        InputStream in = null;
        try {
            logger.debug("加载配置文件[{}]开始", path);
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

            Properties props = new Properties();
            props.load(in);

            fileProperties.put(FileUtils.name(path), props);
            props2Map(props, properties);
            logger.debug("加载配置文件[{}]完成", path);
        }
        catch (Exception e) {
            logger.error("加载属性文件出错", e);
        }
        finally {
            try {
                if (null != in)
                    in.close();
            }
            catch (Exception e) {
            }
        }
    }

    private static void props2Map(Properties props, Map<String, String> map) {
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
    }

    public static String get(String key) {
        return properties.get(key);
    }

    public static Integer getInt(String key) {
        return Integer.valueOf(get(key));
    }

    public static Long getLong(String key) {
        return Long.valueOf(get(key));
    }

    public static Double getDouble(String key) {
        return Double.valueOf(get(key));
    }

    public static String getOrDefault(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    public static String file(String file, String key) {
        Properties pros = fileProperties.get(file);
        if (null == pros)
            return null;

        return pros.getProperty(key);
    }

    public static String fileOrDefault(String file, String key, String defaultValue) {
        Properties pros = fileProperties.get(file);
        if (null == pros)
            return null;

        return pros.getProperty(key, defaultValue);
    }
}
