package com.github.superzhc.data.snowball.entity;

/**
 * @author superz
 * @create 2021/8/2 18:09
 */
public class ReportEarningforecast {
    private Double eps;
    private String roe;
    private String pb;
    private Double pe;
    private String forecastYear;

    public Double getEps() {
        return this.eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    public String getRoe() {
        return this.roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
    }

    public String getPb() {
        return this.pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public Double getPe() {
        return this.pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    public String getForecastYear() {
        return this.forecastYear;
    }

    public void setForecastYear(String forecastYear) {
        this.forecastYear = forecastYear;
    }
}
