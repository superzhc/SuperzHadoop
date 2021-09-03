package com.github.superzhc.data.snowball.entity;

import java.util.List;

/**
 * @author superz
 * @create 2021/8/3 10:36
 */
public class FinanceBusiness {
    public static class FinanceBusinessItem {
        private String projectAnnouncedName;
        private Double primeOperatingIncome;
        private Double incomeRatio;
        private Double grossProfitRate;

        public String getProjectAnnouncedName() {
            return this.projectAnnouncedName;
        }

        public void setProjectAnnouncedName(String projectAnnouncedName) {
            this.projectAnnouncedName = projectAnnouncedName;
        }

        public Double getPrimeOperatingIncome() {
            return this.primeOperatingIncome;
        }

        public void setPrimeOperatingIncome(Double primeOperatingIncome) {
            this.primeOperatingIncome = primeOperatingIncome;
        }

        public Double getIncomeRatio() {
            return this.incomeRatio;
        }

        public void setIncomeRatio(Double incomeRatio) {
            this.incomeRatio = incomeRatio;
        }

        public Double getGrossProfitRate() {
            return this.grossProfitRate;
        }

        public void setGrossProfitRate(Double grossProfitRate) {
            this.grossProfitRate = grossProfitRate;
        }
    }

    public static class FinanceBusinessClass {
        private Integer classStandard;
        private List<FinanceBusinessItem> businessList;

        public Integer getClassStandard() {
            return this.classStandard;
        }

        public void setClassStandard(Integer classStandard) {
            this.classStandard = classStandard;
        }

        public List<FinanceBusinessItem> getBusinessList() {
            return this.businessList;
        }

        public void setBusinessList(List<FinanceBusinessItem> businessList) {
            this.businessList = businessList;
        }
    }

    public static class FinanceBusinessReport {
        private Integer reportDate;
        private String reportName;
        private List<FinanceBusinessClass> classList;

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

        public List<FinanceBusinessClass> getClassList() {
            return this.classList;
        }

        public void setClassList(List<FinanceBusinessClass> classList) {
            this.classList = classList;
        }
    }

    private String quoteName;
    private String currency;
    private List<FinanceBusinessReport> list;
    private String currencyCode;

    public String getQuoteName() {
        return this.quoteName;
    }

    public void setQuoteName(String quoteName) {
        this.quoteName = quoteName;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<FinanceBusinessReport> getList() {
        return this.list;
    }

    public void setList(List<FinanceBusinessReport> list) {
        this.list = list;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
