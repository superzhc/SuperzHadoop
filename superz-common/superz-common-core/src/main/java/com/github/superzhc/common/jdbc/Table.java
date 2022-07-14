package com.github.superzhc.common.jdbc;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
    String name() default "";

//    String schema() default "";
//
//    String catalog() default "";

    String comment() default "";
}
