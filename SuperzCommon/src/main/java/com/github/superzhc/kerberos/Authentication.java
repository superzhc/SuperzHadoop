package com.github.superzhc.kerberos;

import com.github.superzhc.util.StringUtils;

import java.io.File;

/**
 * 2020年10月15日 superz add
 */
public class Authentication
{
    // region
    private static final String CONF_NAME = "krb5.conf";// TODO window下是krb5.ini文件
    private static final String CONF_PROPERTY_PARAM = "java.security.krb5.conf";
    // endregion

    public static void main(String[] args) {
        // System.out.println(System.getProperty("user.home"));
    }

    public void test() {
        String path = null;
        String default_path = System.getProperty("user.home") + File.separator + CONF_NAME;
        if (new File(default_path).exists())
            path = default_path;

        // TODO 读取资源目录下的配置

        // 读取系统的配置参数
        String conf_property = System.getProperty(CONF_PROPERTY_PARAM);
        if (!StringUtils.isBlank(conf_property))
            path = conf_property;

        if (!StringUtils.isBlank(path))
            System.setProperty(CONF_PROPERTY_PARAM, path);
    }
}
