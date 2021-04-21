package com.github.superzhc.flink.manage.util;

import cn.hutool.core.util.StrUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/4/9 15:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class Result<T> implements Serializable {
    public static final Integer SUCCESS = 0;
    public static final Integer FAIL = 1;

    private int code = SUCCESS;
    private String msg = "success";
    private T data;
    /**
     * 额外数据
     */
    private Map<String, Object> extra;

    public Result(T data) {
        this.data = data;
    }

    public Result(T data, Map<String, Object> extra) {
        this.data = data;
        this.extra = extra;
    }

    public Result(T data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public Result(T data, String msg, Map<String, Object> extra) {
        this.data = data;
        this.msg = msg;
        this.extra = extra;
    }

    public Result(Throwable e) {
        this.msg = e.getMessage();
        this.code = FAIL;
    }

    public void extra(String key, Object value) {
        if (null == extra) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>(data);
        return r;
    }

    public static <T> Result<T> success(T data, Map<String, Object> extra) {
        Result<T> r = new Result<>(data, extra);
        return r;
    }

    /**
     * 消息支持Slf4j的模板写法
     *
     * @param msg
     * @param args
     * @return
     */
    public static Result<Object> success(String msg, Object... args) {
        Result r = new Result();
        r.setMsg(StrUtil.format(msg, args));
        return r;
    }

    /**
     * 消息支持Slf4j的模板写法
     *
     * @param msg
     * @param args
     * @return
     */
    public static Result fail(String msg, Object... args) {
        return fail(FAIL, msg, args);
    }

    /**
     * 消息支持Slf4j的模板写法
     *
     * @param errorCode
     * @param msg
     * @param args
     * @return
     */
    public static Result fail(Integer errorCode, String msg, Object... args) {
        Result r = new Result();
        r.setCode(errorCode);
        r.setMsg(StrUtil.format(msg, args));
        return r;
    }

    public static Result fail(Throwable e) {
        return new Result(e);
    }
}
