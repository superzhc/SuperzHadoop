package com.github.superzhc.xxljob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author superz
 * @create 2021/7/21 9:31
 */
@Configuration
@ConfigurationProperties(prefix = "bigdata")
public class BigdataConfig {
    private Map<String, String> lib;

    private Map<String, String> component;

    public Map<String, String> getLib() {
        return lib;
    }

    public void setLib(Map<String, String> lib) {
        this.lib = lib;
    }

    public Map<String, String> getComponent() {
        return component;
    }

    public void setComponent(Map<String, String> component) {
        this.component = component;
    }

    public String getComponent(String component) {
        return getComponent().get(component);
    }
}
