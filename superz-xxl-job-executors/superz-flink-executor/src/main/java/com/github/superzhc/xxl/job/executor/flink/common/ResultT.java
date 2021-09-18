package com.github.superzhc.xxl.job.executor.flink.common;

import java.text.MessageFormat;

/**
 * @author superz
 * @create 2021/7/20 19:54
 */
public class ResultT {
    public static final Integer DEFAULT_SUCCESS_CODE = 0;
    public static final Integer DEFAULT_FAIL_CODE = 1;

    private int code;
    private String msg;
    private Object data;

    public static ResultT success() {
        return success(null);
    }

    public static ResultT success(Object data) {
        return success(DEFAULT_SUCCESS_CODE, data);
    }

    public static ResultT success(int code, Object data) {
        return create(code, null, data);
    }

    public static ResultT fail(Throwable e) {
        return fail(DEFAULT_FAIL_CODE, e);
    }

    public static ResultT fail(int code, Throwable e) {
        return create(code, e.toString(), null);
    }

    public static ResultT fail(String msg, Object... params) {
        return fail(DEFAULT_FAIL_CODE, msg, params);
    }

    public static ResultT fail(int code, String msg, Object... params) {
        return create(code, MessageFormat.format(msg, params), null);
    }

    public static ResultT msg(int code, String msg, Object... params) {
        return create(code, MessageFormat.format(msg, params), null);
    }

    private static ResultT create(int code, String msg, Object data) {
        ResultT r = new ResultT();
        r.setCode(code);
        if (null != msg) {
            r.setMsg(msg);
        }
        if (null != data) {
            r.setData(data);
        }
        return r;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
