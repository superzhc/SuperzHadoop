package com.github.superzhc.data.snowball.entity;

/**
 * @author superz
 * @create 2021/8/2 19:34
 */
public class CapitalBlocktrans {
    private Integer vol;
    private String sellBranchOrgName;
    private Integer premiumRat;
    private Integer transAmt;
    private Integer tdDate;
    private String buyBranchOrgName;
    private Double transPrice;

    public Integer getVol() {
        return this.vol;
    }

    public void setVol(Integer vol) {
        this.vol = vol;
    }

    public String getSellBranchOrgName() {
        return this.sellBranchOrgName;
    }

    public void setSellBranchOrgName(String sellBranchOrgName) {
        this.sellBranchOrgName = sellBranchOrgName;
    }

    public Integer getPremiumRat() {
        return this.premiumRat;
    }

    public void setPremiumRat(Integer premiumRat) {
        this.premiumRat = premiumRat;
    }

    public Integer getTransAmt() {
        return this.transAmt;
    }

    public void setTransAmt(Integer transAmt) {
        this.transAmt = transAmt;
    }

    public Integer getTdDate() {
        return this.tdDate;
    }

    public void setTdDate(Integer tdDate) {
        this.tdDate = tdDate;
    }

    public String getBuyBranchOrgName() {
        return this.buyBranchOrgName;
    }

    public void setBuyBranchOrgName(String buyBranchOrgName) {
        this.buyBranchOrgName = buyBranchOrgName;
    }

    public Double getTransPrice() {
        return this.transPrice;
    }

    public void setTransPrice(Double transPrice) {
        this.transPrice = transPrice;
    }
}
