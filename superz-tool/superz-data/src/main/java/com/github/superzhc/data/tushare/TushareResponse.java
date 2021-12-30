package com.github.superzhc.data.tushare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author superz
 * @create 2021/9/26 10:33
 */
public class TushareResponse {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String[] fields;
        private Object[][] items;

        @JsonProperty("has_more")
        private Boolean hasMore;

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

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }
    }

    @JsonProperty("request_id")
    private String requestId;
    private Integer code;
    private String msg;
    private Data data;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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
