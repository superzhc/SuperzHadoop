package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/3 10:36
 */
public class FinanceIndicator {
    public static class FinanceIndicatorItem {
        private Integer reportDate;
        private String reportName;
        private List<Double> avgRoe;
        private List<Double> npPerShare;
        private List<Double> operateCashFlowPs;
        private List<Double> basicEps;
        private List<Double> capitalReserve;
        private List<Double> undistriProfitPs;
        private List<Double> netInterestOfTotalAssets;
        private List<Double> grossSellingRate;

        public Integer getReportDate() {
            return this.reportDate;
        }

        public void setReportDate(Integer reportDate) {
            this.reportDate = reportDate;
        }

        public String getReportName() {
            return this.reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public List<Double> getAvgRoe() {
            return this.avgRoe;
        }

        public void setAvgRoe(List<Double> avgRoe) {
            this.avgRoe = avgRoe;
        }

        public List<Double> getNpPerShare() {
            return this.npPerShare;
        }

        public void setNpPerShare(List<Double> npPerShare) {
            this.npPerShare = npPerShare;
        }

        public List<Double> getOperateCashFlowPs() {
            return this.operateCashFlowPs;
        }

        public void setOperateCashFlowPs(List<Double> operateCashFlowPs) {
            this.operateCashFlowPs = operateCashFlowPs;
        }

        public List<Double> getBasicEps() {
            return this.basicEps;
        }

        public void setBasicEps(List<Double> basicEps) {
            this.basicEps = basicEps;
        }

        public List<Double> getCapitalReserve() {
            return this.capitalReserve;
        }

        public void setCapitalReserve(List<Double> capitalReserve) {
            this.capitalReserve = capitalReserve;
        }

        public List<Double> getUndistriProfitPs() {
            return this.undistriProfitPs;
        }

        public void setUndistriProfitPs(List<Double> undistriProfitPs) {
            this.undistriProfitPs = undistriProfitPs;
        }

        public List<Double> getNetInterestOfTotalAssets() {
            return this.netInterestOfTotalAssets;
        }

        public void setNetInterestOfTotalAssets(List<Double> netInterestOfTotalAssets) {
            this.netInterestOfTotalAssets = netInterestOfTotalAssets;
        }

        public List<Double> getGrossSellingRate() {
            return this.grossSellingRate;
        }

        public void setGrossSellingRate(List<Double> grossSellingRate) {
            this.grossSellingRate = grossSellingRate;
        }
    }

    private String quoteName;
    private String currencyName;
    private Integer orgType;
    private String lastReportName;
    private String currency;
    private List<FinanceIndicatorItem> list;

    public String getQuoteName() {
        return this.quoteName;
    }

    public void setQuoteName(String quoteName) {
        this.quoteName = quoteName;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getOrgType() {
        return this.orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public String getLastReportName() {
        return this.lastReportName;
    }

    public void setLastReportName(String lastReportName) {
        this.lastReportName = lastReportName;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<FinanceIndicatorItem> getList() {
        return this.list;
    }

    public void setList(List<FinanceIndicatorItem> list) {
        this.list = list;
    }
}
