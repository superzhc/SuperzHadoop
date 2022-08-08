package com.github.superzhc.financial.data.report;

import tech.tablesaw.api.Table;

import java.net.URL;

/**
 * @author superz
 * @create 2022/4/6 11:12
 **/
public class NetEase {
    /**
     * 报表数据都是csv文件流
     */
    //主要财务指标
    private static final String mainFinanceReport = "http://quotes.money.163.com/service/zycwzb_%s.html?type=report";
    //盈利能力
    private static final String profitReport = "http://quotes.money.163.com/service/zycwzb_%s.html?type=report&part=ylnl";
    //偿还能力
    private static final String debtReport = "http://quotes.money.163.com/service/zycwzb_%s.html?type=report&part=chnl";
    //成长能力
    private static final String growReport = "http://quotes.money.163.com/service/zycwzb_%s.html?type=report&part=cznl";
    //营运能力
    private static final String operateReport = "http://quotes.money.163.com/service/zycwzb_%s.html?type=report&part=yynl";
    //财务报表摘要
    private static final String abstractFinanceReport = "http://quotes.money.163.com/service/cwbbzy_%s.html";

    public static void main(String[] args) throws Exception {
        Table table=Table.read().csv(new URL(String.format(mainFinanceReport,"000001")));
        System.out.println(table.print());
    }
}
