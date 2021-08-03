package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/2 19:30
 */
public class CapitalHistory {
    public static class CapitalHistoryItem{
        private Long timestamp;
        private Long amount;

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
    }

    private Long sum3;
    private Long sum5;
    private Long sum10;
    private Long sum20;
    private List<CapitalHistoryItem> items;

    public Long getSum3() {
        return sum3;
    }

    public void setSum3(Long sum3) {
        this.sum3 = sum3;
    }

    public Long getSum5() {
        return sum5;
    }

    public void setSum5(Long sum5) {
        this.sum5 = sum5;
    }

    public Long getSum10() {
        return sum10;
    }

    public void setSum10(Long sum10) {
        this.sum10 = sum10;
    }

    public Long getSum20() {
        return sum20;
    }

    public void setSum20(Long sum20) {
        this.sum20 = sum20;
    }

    public List<CapitalHistoryItem> getItems() {
        return items;
    }

    public void setItems(List<CapitalHistoryItem> items) {
        this.items = items;
    }
}
