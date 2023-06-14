package com.github.superzhc.web.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MyApplication extends Application<MyApplicationConfiguration> {
    @Override
    public void run(MyApplicationConfiguration configuration, Environment environment) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }
}
