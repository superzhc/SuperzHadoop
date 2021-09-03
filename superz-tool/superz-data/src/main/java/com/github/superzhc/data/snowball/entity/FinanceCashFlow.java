package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/3 10:23
 */
public class FinanceCashFlow {
    public static class FinanceCashFlowItem {
        private Integer reportDate;
        private String reportName;
        private List<Double> ncfFromOa;
        private List<Double> ncfFromIa;
        private List<Double> ncfFromFa;

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

        public List<Double> getNcfFromOa() {
            return this.ncfFromOa;
        }

        public void setNcfFromOa(List<Double> ncfFromOa) {
            this.ncfFromOa = ncfFromOa;
        }

        public List<Double> getNcfFromIa() {
            return this.ncfFromIa;
        }

        public void setNcfFromIa(List<Double> ncfFromIa) {
            this.ncfFromIa = ncfFromIa;
        }

        public List<Double> getNcfFromFa() {
            return this.ncfFromFa;
        }

        public void setNcfFromFa(List<Double> ncfFromFa) {
            this.ncfFromFa = ncfFromFa;
        }
    }

    private String quoteName;
    private String currencyName;
    private Integer orgType;
    private String lastReportName;
    private String currency;
    private List<FinanceCashFlowItem> list;

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

    public List<FinanceCashFlowItem> getList() {
        return this.list;
    }

    public void setList(List<FinanceCashFlowItem> list) {
        this.list = list;
    }
}
