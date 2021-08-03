package com.github.superzhc.data.snowball.entity;

/**
 * @author superz
 * @create 2021/8/2 19:33
 */
public class CapitalAssort {
    private Integer sellLarge;
    private Integer sellMedium;
    private Integer sellSmall;
    private Integer sellTotal;
    private Integer buyLarge;
    private Integer buyMedium;
    private Integer buySmall;
    private Integer buyTotal;
    private Integer timestamp;
    private String createdAt;
    private String updatedAt;

    public Integer getSellLarge() {
        return this.sellLarge;
    }

    public void setSellLarge(Integer sellLarge) {
        this.sellLarge = sellLarge;
    }

    public Integer getSellMedium() {
        return this.sellMedium;
    }

    public void setSellMedium(Integer sellMedium) {
        this.sellMedium = sellMedium;
    }

    public Integer getSellSmall() {
        return this.sellSmall;
    }

    public void setSellSmall(Integer sellSmall) {
        this.sellSmall = sellSmall;
    }

    public Integer getSellTotal() {
        return this.sellTotal;
    }

    public void setSellTotal(Integer sellTotal) {
        this.sellTotal = sellTotal;
    }

    public Integer getBuyLarge() {
        return this.buyLarge;
    }

    public void setBuyLarge(Integer buyLarge) {
        this.buyLarge = buyLarge;
    }

    public Integer getBuyMedium() {
        return this.buyMedium;
    }

    public void setBuyMedium(Integer buyMedium) {
        this.buyMedium = buyMedium;
    }

    public Integer getBuySmall() {
        return this.buySmall;
    }

    public void setBuySmall(Integer buySmall) {
        this.buySmall = buySmall;
    }

    public Integer getBuyTotal() {
        return this.buyTotal;
    }

    public void setBuyTotal(Integer buyTotal) {
        this.buyTotal = buyTotal;
    }

    public Integer getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
