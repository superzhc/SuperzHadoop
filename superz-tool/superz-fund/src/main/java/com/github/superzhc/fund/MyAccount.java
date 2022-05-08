package com.github.superzhc.fund;

import com.github.superzhc.fund.data.fund.EastMoneyFund;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static tech.tablesaw.aggregate.AggregateFunctions.sum;

/**
 * @author superz
 * @create 2022/4/24 10:08
 **/
public class MyAccount {
    public static void main(String[] args) throws IOException {
        Table table = Table.read().csv(MyAccount.class.getClassLoader().getResourceAsStream("week.csv"));
        table = table.replaceColumn("code",
                table.intColumn("code").asStringColumn().padStart(6, '0').setName("code"));

        table=table.replaceColumn("amount",table.intColumn("amount").asDoubleColumn().setName("amount"));


        Table records = Table.create(
                DateColumn.create("date"),
                StringColumn.create("code"),
                // 净值
                DoubleColumn.create("net_worth"),
                // 份额
                DoubleColumn.create("share"),
                // 成本（包含费率）
                DoubleColumn.create("amount")
        );

        Set<String> codes = table.stringColumn("code").asSet();
        for (String code : codes) {
            //String code = "160119";
            Table info = EastMoneyFund.fundNew(code);

            // 获取费率
            String realRate = info.row(0).getString("real_rate");
            double rr = Double.parseDouble(realRate.substring(0, realRate.length() - 1)) / 100.0;
//            System.out.println("real_rate:" + rr);

            // 获取历史值
            Table history = EastMoneyFund.fundNetHistory(code);

            Table t3 = table.where(table.stringColumn("code").isEqualTo(code))
                    .joinOn("date")
                    .inner(history)
                    .select("date", "code", "net_worth", "amount");

            // 计算每笔的份额
            DoubleColumn shareColumn = t3.doubleColumn("amount")
                    // 每笔的净投入：总投入/(1+费率)
                    .divide(1 + rr)
                    // 份额：净投入/单位净值
                    .divide(t3.doubleColumn("net_worth")).setName("share");
            t3.addColumns(shareColumn);

            records.append(t3);
        }

        System.out.println(records.printAll());

        Table summarize = records.summarize("share", "amount", sum).by("code");
        summarize.addColumns(summarize.doubleColumn(1).divide(summarize.doubleColumn(2)).setName("avg_net_worth"));

        //summarize.addColumns(DoubleColumn.create("current_net_worth", summarize.column(0).size()));
        Table currentNetWorthT = Table.create(
                StringColumn.create("code"),
                DoubleColumn.create("current_net_worth")
        );
        for (String code : codes) {
            // 获取实时净值
            Table realNetWorth = EastMoneyFund.fundRealNet(code);
            double currentNetWorth;
            if (LocalDate.now().isEqual(realNetWorth.row(0).getDate("latest_date"))) {
                currentNetWorth = realNetWorth.row(0).getDouble("latest_net_worth");
            } else {
                LocalDateTime estimateDate = LocalDateTime.parse(
                        realNetWorth.row(0).getString("estimate_date"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                );

                if (LocalDate.now().isEqual(estimateDate.toLocalDate())) {
                    currentNetWorth = realNetWorth.row(0).getDouble("estimate_net_worth");
                } else {
                    currentNetWorth = realNetWorth.row(0).getDouble("latest_net_worth");
                }
            }

            currentNetWorthT.column("current_net_worth").appendObj(currentNetWorth);
            currentNetWorthT.column("code").appendObj(code);
        }

        summarize = summarize.joinOn("code").inner(currentNetWorthT);

        summarize.addColumns(summarize.doubleColumn(2).multiply(summarize.doubleColumn("current_net_worth")));

        summarize.addColumns(summarize.doubleColumn(5).subtract(summarize.doubleColumn(1)).divide(summarize.doubleColumn(1)).setName("syl"));

        System.out.println(summarize.printAll());


//        System.out.println(t3.summarize("share", sum).by("code"));
//
//        double share = t3.summarize("share", sum).by("code").row(0).getDouble(1);
//        double latestNetWorth = fundHistory.row(0).getDouble("net_worth");
//        double total = share * latestNetWorth;
//        System.out.printf("%f*%f=%f", share, latestNetWorth, total);
//
//        table.replaceColumn("amount",table.doubleColumn("amount").multiply(-1).setName("amount"));
//
//        table.dateColumn("date").append(LocalDate.now());
//        table.intColumn("code").append(Integer.valueOf(code));
//        table.doubleColumn("amount").append(total);
//
//        System.out.println(table.print());
//
//        double rate= IndexTool.syl(table,"date","amount");
//        System.out.println(rate);
    }
}
