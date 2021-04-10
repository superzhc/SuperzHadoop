package com.github.superzhc.flink.manage.annotation;

import java.lang.annotation.*;

/**
 * @author superz
 * @create 2021/4/10 14:22
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CLIOption {
    String shortening();

    /**
     * 默认情况下是跟字段名是一样的，除非一些名称是Java的关键字，无法直接为字段名，这种情况毕竟少数，因此给与一个空的默认值比较合理点
     *
     * @return
     */
    String name() default "";

    String description() default "";

    boolean isProperty() default false;
}