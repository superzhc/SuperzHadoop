package com.github.superzhc.common.cron.annotation;

import java.lang.annotation.*;
import java.util.Map;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface STask {
    String group();

    String name();

    String cron();

    String description();
}
