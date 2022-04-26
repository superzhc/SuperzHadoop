package com.github.superzhc.fund.backtesting;

import com.github.superzhc.fund.akshare.DanJuanFunds;
import com.github.superzhc.fund.akshare.ENiu;
import com.github.superzhc.fund.strategy.PurchaseAnalysis;
import com.github.superzhc.fund.tablesaw.utils.MyAggregateFunctions;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

/**
 * @author superz
 * @create 2022/4/26 17:13
 **/
public class PEData {
    private static Logger log = LoggerFactory.getLogger(PEData.class);

    private String indexCode;
    private double maxPosition;

    public PEData(String indexCode, double maxPosition) {
        this.indexCode = indexCode;
        this.maxPosition = maxPosition;
    }

    public Table create(Integer days) {
        LocalDate end = LocalDate.now();
        log.debug("Last Day:{}", end);
        LocalDate start = end.minusDays(days);
        log.debug("Start Day:{}", start);

        Table table = DanJuanFunds.peHistory10Y(indexCode);//ENiu.indexHistory(indexCode);
        table = table.where(table.dateColumn("date").isAfter(start));

        table.addColumns(MyAggregateFunctions.position(table.doubleColumn("pe")).setName("position"));

        table = table.where(table.doubleColumn("position").isLessThanOrEqualTo(maxPosition));

        table = table.select("date");
        return table;
    }

    public static void main(String[] args) {
        String code = "160119";
        String indexCode = "000905.SH";

        Table table = new PEData(indexCode, 0.05).create(365);
        TableUtils.addConstantColumn(table, "code", code);
        TableUtils.addConstantColumn(table, "amount", 200.0);

        PurchaseAnalysis analysis = new PurchaseAnalysis(table);
        System.out.println(analysis.getRecords().print());
        Table purchaseSummarize = analysis.summarize();
        System.out.println(purchaseSummarize.print());

        //-0.24597204000099043
    }
}
