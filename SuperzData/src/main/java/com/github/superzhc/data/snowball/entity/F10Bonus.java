package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/3 10:49
 */
public class F10Bonus {
    public static class F10BonusAddtion {
        private Integer actualIssueVol;
        private Double actualIssuePrice;
        private Integer listingAd;
        private Integer actualRcNetAmt;

        public Integer getActualIssueVol() {
            return this.actualIssueVol;
        }

        public void setActualIssueVol(Integer actualIssueVol) {
            this.actualIssueVol = actualIssueVol;
        }

        public Double getActualIssuePrice() {
            return this.actualIssuePrice;
        }

        public void setActualIssuePrice(Double actualIssuePrice) {
            this.actualIssuePrice = actualIssuePrice;
        }

        public Integer getListingAd() {
            return this.listingAd;
        }

        public void setListingAd(Integer listingAd) {
            this.listingAd = listingAd;
        }

        public Integer getActualRcNetAmt() {
            return this.actualRcNetAmt;
        }

        public void setActualRcNetAmt(Integer actualRcNetAmt) {
            this.actualRcNetAmt = actualRcNetAmt;
        }
    }

    public static class F10BonusItem {
        private String dividendYear;
        private String ashareExDividendDate;
        private String planExplain;
        private String cancleDividendDate;

        public String getDividendYear() {
            return this.dividendYear;
        }

        public void setDividendYear(String dividendYear) {
            this.dividendYear = dividendYear;
        }

        public String getAshareExDividendDate() {
            return this.ashareExDividendDate;
        }

        public void setAshareExDividendDate(String ashareExDividendDate) {
            this.ashareExDividendDate = ashareExDividendDate;
        }

        public String getPlanExplain() {
            return this.planExplain;
        }

        public void setPlanExplain(String planExplain) {
            this.planExplain = planExplain;
        }

        public String getCancleDividendDate() {
            return this.cancleDividendDate;
        }

        public void setCancleDividendDate(String cancleDividendDate) {
            this.cancleDividendDate = cancleDividendDate;
        }
    }

    private List<F10BonusAddtion> addtions;
    private List<String> allots;
    private List<F10BonusItem> items;

    public List<F10BonusAddtion> getAddtions() {
        return this.addtions;
    }

    public void setAddtions(List<F10BonusAddtion> addtions) {
        this.addtions = addtions;
    }

    public List<String> getAllots() {
        return this.allots;
    }

    public void setAllots(List<String> allots) {
        this.allots = allots;
    }

    public List<F10BonusItem> getItems() {
        return this.items;
    }

    public void setItems(List<F10BonusItem> items) {
        this.items = items;
    }
}
