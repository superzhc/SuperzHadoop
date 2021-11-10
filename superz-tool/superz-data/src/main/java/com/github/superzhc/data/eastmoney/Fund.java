package com.github.superzhc.data.eastmoney;

/**
 * @author superz
 * @create 2021/10/29 14:10
 */
public class Fund {
    private static final String URL = "http://fund.eastmoney.com";

    private static final String COMPANY_URL = URL + "/js/jjjz_gs.js?dt=";
    private static final String FUNDCODE_URL = URL + "/js/fundcode_search.js";


    public void company() {
        String result = "";
        result.substring("var gs=".length());
    }

    public void fundcode() {
        String result = "";
        result.substring("var r = ".length(), result.length() - 1);
    }
}