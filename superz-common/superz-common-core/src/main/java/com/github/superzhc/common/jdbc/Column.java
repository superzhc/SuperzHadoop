package com.github.superzhc.common.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {
    /**
     * 字段名
     *
     * @return
     */
    String name() default "";

    /**
     * 字段类型
     *
     * @return
     */
    String type() default "";

    /**
     * 字段长度，默认是 255
     *
     * @return
     */
    int length() default 255;

    /**
     * 精度
     *
     * @return
     */
    int precision() default 0;

    /**
     * 小数位数
     *
     * @return
     */
    int scale() default 0;

    boolean nullable() default true;

    /**
     * 主键，默认为false
     *
     * @return
     */
    boolean primaryKey() default false;

    /**
     * 自增，默认为false
     *
     * @return
     */
    boolean autoIncrement() default false;

    /**
     * 默认值
     *
     * @return
     */
    String defaultValue() default "";

    /**
     * 注释
     *
     * @return
     */
    String comment() default "";
}
