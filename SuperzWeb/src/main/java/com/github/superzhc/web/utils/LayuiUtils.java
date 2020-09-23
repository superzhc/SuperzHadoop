package com.github.superzhc.web.utils;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Layui组件返回值的工具类
 * 2020年09月05日 superz add
 */
public class LayuiUtils
{
    public static Result msg_ok(String msg) {
        return msg(0, msg);
    }

    public static Result msg_error(String msg) {
        return msg(1, msg);
    }

    public static Result msg(Integer code, String msg) {
        MessageResult result = new MessageResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result data(T t) {
        DataResult result = new DataResult();
        result.setData(t);
        return result;
    }

    @Deprecated
    public static <T> Result model_ok(T data) {
        DataResult result = new DataResult();
        result.setData(data);
        return result;
    }

    @Deprecated
    public static Result form_ok(String msg) {
        MessageResult result = new MessageResult();
        result.setMsg(msg);
        return result;
    }

    @Deprecated
    public static Result form_error(String msg) {
        MessageResult result = new MessageResult();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result table_ok(IPage<T> pageInfo) {
        TableResult result = new TableResult();
        result.setCount(pageInfo.getTotal());
        result.setData(pageInfo.getRecords());
        return result;
    }

    public static <T> Result table_ok(List<T> list) {
        TableResult result = new TableResult();
        result.setCount(list.size());
        result.setData(list);
        return result;
    }

    public static Result table_error(String msg) {
        TableResult result = new TableResult();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    private static class DataResult extends Result
    {
        private Object data;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    private static class MessageResult extends Result
    {
        private String msg = "";

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    private static class TableResult extends Result
    {

        /* 列表返回的数据的条数 */
        private long count = 0;
        /* 返回的数据 */
        private Object data = null;

        private String msg = "";

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

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
