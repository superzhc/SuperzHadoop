package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/2 19:24
 */
public class CapitalFlow {
    public static class CapitalFlowItem {
        private Long timestamp;
        private Long amount;
        private String type;

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private String symbol;
    private List<CapitalFlowItem> items;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<CapitalFlowItem> getItems() {
        return items;
    }

    public void setItems(List<CapitalFlowItem> items) {
        this.items = items;
    }
}
