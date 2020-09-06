package com.github.superzhc.web.utils;

import com.github.pagehelper.PageInfo;

/**
 * Layui组件返回值的工具类
 * 2020年09月05日 superz add
 */
public class LayuiUtils
{
    public static <T> Result model_ok(T data) {
        ModelResult result = new ModelResult();
        result.setData(data);
        return result;
    }

    public static Result model_error(String msg) {
        ModelResult result = new ModelResult();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    public static Result form_ok(String msg) {
        FormResult result = new FormResult();
        result.setMsg(msg);
        return result;
    }

    public static Result form_error(String msg) {
        FormResult result = new FormResult();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result list_ok(PageInfo<T> pageInfo) {
        TableResult result = new TableResult();
        result.setCount(pageInfo.getTotal());
        result.setData(pageInfo.getList());
        return result;
    }

    public static Result list_error(String msg) {
        TableResult result = new TableResult();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    private static class ModelResult extends Result
    {
        private Object data;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    private static class FormResult extends Result
    {
    }

    private static class TableResult extends Result
    {

        /* 列表返回的数据的条数 */
        private long count = 0;
        /* 返回的数据 */
        private Object data = null;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
