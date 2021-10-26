package com.github.superzhc.data.tushare;

/**
 * @author superz
 * @create 2021/9/26 10:33
 */
public class TushareResponse {
    public static class Data {
        private String[] fields;
        private Object[][] items;

        public String[] getFields() {
            return fields;
        }

        public void setFields(String[] fields) {
            this.fields = fields;
        }

        public Object[][] getItems() {
            return items;
        }

        public void setItems(Object[][] items) {
            this.items = items;
        }
    }

    private Integer code;
    private String msg;
    private Data data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
