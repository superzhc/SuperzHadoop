package com.github.superzhc.web.dropwizard;

import com.codahale.metrics.health.HealthCheck;

/**
 * 自定义健康检查
 */
public class MyHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
