package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/3 10:36
 */
public class FinanceIncome {
    public static class FinanceIncomeItem {
        private Integer reportDate;
        private String reportName;
        private List<Double> netProfit;
        private List<Double> netProfitAtsopc;
        private List<Double> totalRevenue;
        private List<Double> op;

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

        public List<Double> getNetProfit() {
            return this.netProfit;
        }

        public void setNetProfit(List<Double> netProfit) {
            this.netProfit = netProfit;
        }

        public List<Double> getNetProfitAtsopc() {
            return this.netProfitAtsopc;
        }

        public void setNetProfitAtsopc(List<Double> netProfitAtsopc) {
            this.netProfitAtsopc = netProfitAtsopc;
        }

        public List<Double> getTotalRevenue() {
            return this.totalRevenue;
        }

        public void setTotalRevenue(List<Double> totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public List<Double> getOp() {
            return this.op;
        }

        public void setOp(List<Double> op) {
            this.op = op;
        }
    }

    private String quoteName;
    private String currencyName;
    private Integer orgType;
    private String lastReportName;
    private String currency;
    private List<FinanceIncomeItem> list;

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

    public List<FinanceIncomeItem> getList() {
        return this.list;
    }

    public void setList(List<FinanceIncomeItem> list) {
        this.list = list;
    }
}
