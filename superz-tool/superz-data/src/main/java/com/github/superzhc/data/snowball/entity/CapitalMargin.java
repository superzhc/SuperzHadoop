package com.github.superzhc.data.snowball.entity;

/**
 * @author superz
 * @create 2021/8/2 19:35
 */
public class CapitalMargin {
    private Integer marginTradingAmtBalance;
    private Integer shortSellingAmtBalance;
    private Integer marginTradingBalance;
    private Integer tdDate;

    public Integer getMarginTradingAmtBalance() {
        return this.marginTradingAmtBalance;
    }

    public void setMarginTradingAmtBalance(Integer marginTradingAmtBalance) {
        this.marginTradingAmtBalance = marginTradingAmtBalance;
    }

    public Integer getShortSellingAmtBalance() {
        return this.shortSellingAmtBalance;
    }

    public void setShortSellingAmtBalance(Integer shortSellingAmtBalance) {
        this.shortSellingAmtBalance = shortSellingAmtBalance;
    }

    public Integer getMarginTradingBalance() {
        return this.marginTradingBalance;
    }

    public void setMarginTradingBalance(Integer marginTradingBalance) {
        this.marginTradingBalance = marginTradingBalance;
    }

    public Integer getTdDate() {
        return this.tdDate;
    }

    public void setTdDate(Integer tdDate) {
        this.tdDate = tdDate;
    }
}
