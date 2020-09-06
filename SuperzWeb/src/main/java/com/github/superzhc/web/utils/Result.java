package com.github.superzhc.web.utils;

/**
 * 2020年09月05日 superz add
 */
public abstract class Result
{
    /* 0：代表成功 */
    private int code = 0;

    /* 返回的错误数据 */
    private String msg = "";

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
}
