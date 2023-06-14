package com.github.superzhc.web.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * 每个Dropwizard应用程序都有自己的Configuration类的子类，它指定特定的环境参数。这些参数在YAML配置文件中指定，该文件被反序列化为应用程序配置类的实例。
 */
public class MyApplicationConfiguration extends Configuration {
    private String serviceName;

    @JsonProperty
    public String getServiceName() {
        return serviceName;
    }

    @JsonProperty
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
