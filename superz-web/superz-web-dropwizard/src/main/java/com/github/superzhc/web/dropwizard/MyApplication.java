package com.github.superzhc.web.dropwizard;

import com.github.superzhc.web.dropwizard.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MyApplication extends Application<MyApplicationConfiguration> {
    @Override
    public void run(MyApplicationConfiguration configuration, Environment environment) throws Exception {
        // 添加健康检查
        environment.healthChecks().register("my_health_check", new MyHealthCheck());

        // 注册资源
        final HelloWorldResource helloWorldResource = new HelloWorldResource();
        environment.jersey().register(helloWorldResource);
    }

    public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }
}
