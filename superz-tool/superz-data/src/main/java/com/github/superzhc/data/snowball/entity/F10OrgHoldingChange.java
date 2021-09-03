package com.github.superzhc.data.snowball.entity;

/**
 * @author superz
 * @create 2021/8/3 10:50
 */
public class F10OrgHoldingChange {
    private String chgDate;
    private String institutionNum;
    private Double chg;
    private Double heldRatio;
    private Double price;
    private Integer timestamp;

    public String getChgDate() {
        return this.chgDate;
    }

    public void setChgDate(String chgDate) {
        this.chgDate = chgDate;
    }

    public String getInstitutionNum() {
        return this.institutionNum;
    }

    public void setInstitutionNum(String institutionNum) {
        this.institutionNum = institutionNum;
    }

    public Double getChg() {
        return this.chg;
    }

    public void setChg(Double chg) {
        this.chg = chg;
    }

    public Double getHeldRatio() {
        return this.heldRatio;
    }

    public void setHeldRatio(Double heldRatio) {
        this.heldRatio = heldRatio;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
