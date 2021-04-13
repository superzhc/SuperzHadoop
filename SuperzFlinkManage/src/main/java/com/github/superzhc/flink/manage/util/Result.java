package com.github.superzhc.flink.manage.util;

import cn.hutool.core.util.StrUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.text.MessageFormat;

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

    public Result(T data) {
        this.data = data;
    }

    public Result(T data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public Result(Throwable e) {
        this.msg = e.getMessage();
        this.code = FAIL;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>(data);
        return r;
    }

    /**
     * 消息支持Slf4j的模板写法
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
     * @param msg
     * @param args
     * @return
     */
    public static Result fail(String msg, Object... args) {
        return fail(FAIL, msg, args);
    }

    /**
     * 消息支持Slf4j的模板写法
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
}
