package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/3 10:52
 */
public class F10TopHolders {
    public static class F10TopHoldersTime {
        private String name;
        private Integer value;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return this.value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    public static class F10TopHoldersItem {
        private Double chg;
        private Integer heldNum;
        private Double heldRatio;
        private String holderName;

        public Double getChg() {
            return this.chg;
        }

        public void setChg(Double chg) {
            this.chg = chg;
        }

        public Integer getHeldNum() {
            return this.heldNum;
        }

        public void setHeldNum(Integer heldNum) {
            this.heldNum = heldNum;
        }

        public Double getHeldRatio() {
            return this.heldRatio;
        }

        public void setHeldRatio(Double heldRatio) {
            this.heldRatio = heldRatio;
        }

        public String getHolderName() {
            return this.holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }
    }

    private List<F10TopHoldersTime> times;
    private List<F10TopHoldersItem> items;

    public List<F10TopHoldersTime> getTimes() {
        return this.times;
    }

    public void setTimes(List<F10TopHoldersTime> times) {
        this.times = times;
    }

    public List<F10TopHoldersItem> getItems() {
        return this.items;
    }

    public void setItems(List<F10TopHoldersItem> items) {
        this.items = items;
    }
}
