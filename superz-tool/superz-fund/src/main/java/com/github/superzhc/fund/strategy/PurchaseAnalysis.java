package com.github.superzhc.fund.strategy;

import com.github.superzhc.fund.MyAccount;
import com.github.superzhc.fund.data.fund.EastMoneyFund;
import com.github.superzhc.fund.index.IndexTool;
import tech.tablesaw.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static tech.tablesaw.aggregate.AggregateFunctions.sum;

/**
 * @author superz
 * @create 2022/4/25 9:09
 **/
public class PurchaseAnalysis {
    public Table table;
    public String[] codes = null;
    public Table records = null;

    /**
     * @param table Structure of
     * Index  |  Column Name  |  Column Type  |
     * -----------------------------------------
     * 0  |         date  |       STRING  |
     * 1  |         code  |       STRING  |
     * 2  |         amount  |      DOUBLE  |
     */
    public PurchaseAnalysis(Table table) {
        // table 处理
        if (table.column("code").type() == ColumnType.INTEGER) {
            table = table.replaceColumn("code",
                    table.intColumn("code").asStringColumn().padStart(6, '0').setName("code"));
        }

        if (table.column("amount").type() == ColumnType.INTEGER) {
            table = table.replaceColumn("amount", table.intColumn("amount").asDoubleColumn().setName("amount"));
        }

        this.table = table;
    }

    public String[] getCodes() {
        if (null == codes || codes.length == 0) {
            Set<String> set = table.stringColumn("code").asSet();
            codes = new String[set.size()];
            set.toArray(codes);
        }
        return codes;
    }


    public Table getRecords() {
        if (null == records) {
            records = table.emptyCopy()
                    .addColumns(DoubleColumn.create("net_worth"))
                    .addColumns(DoubleColumn.create("share"));

            for (String code : getCodes()) {
                Table info = EastMoneyFund.fundNew(code);

                // 获取费率
                String realRate = info.row(0).getString("real_rate");
                double rr = Double.parseDouble(realRate.substring(0, realRate.length() - 1)) / 100.0;

                // 获取历史值
                Table history = EastMoneyFund.fundNetHistory(code);

                Table subTable = table.where(table.stringColumn("code").isEqualTo(code))
                        .joinOn("date")
                        .inner(history)
                        .select("date", "code", "net_worth", "amount");

                // 计算每笔的份额
                DoubleColumn shareColumn = subTable.doubleColumn("amount")
                        // 每笔的净投入：总投入/(1+费率)
                        .divide(1 + rr)
                        // 份额：净投入/单位净值
                        .divide(subTable.doubleColumn("net_worth"))
                        .map(d -> {
                            BigDecimal bd = new BigDecimal(d);
                            double d2 = bd.setScale(2, BigDecimal.ROUND_DOWN).doubleValue(); // 保留两位小数，不四舍五入(可选舍入模式)
                            return d2;
                        })
                        .setName("share");

                subTable.addColumns(shareColumn);

                records.append(subTable);
            }
        }
        return records;
    }

    public Table realNetWorth() {
        Table t = EastMoneyFund.fundRealNet(codes);

        DoubleColumn currentNetWorth = DoubleColumn.create("current_net_worth");
        for (int i = 0, count = t.rowCount(); i < count; i++) {
            double rowCurrentNetWorth;
            Row row = t.row(i);
            if (LocalDate.now().isEqual(row.getDate("latest_date"))) {
                rowCurrentNetWorth = row.getDouble("latest_net_worth");
            } else {
                LocalDateTime estimateDate = LocalDateTime.parse(
                        row.getString("estimate_date"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                );

                if (LocalDate.now().isEqual(estimateDate.toLocalDate())) {
                    rowCurrentNetWorth = row.getDouble("estimate_net_worth");
                } else {
                    rowCurrentNetWorth = row.getDouble("latest_net_worth");
                }
            }
            currentNetWorth.append(rowCurrentNetWorth);
        }

        t = t.select("code", "latest_net_worth", "estimate_net_worth")
                .addColumns(currentNetWorth);

        return t;
    }

    public Table summarize() {
        Table t = getRecords()
                .summarize("amount", "share", sum)
                .by("code");

        t.column(sum.functionName() + " [amount]").setName("total_input");
        t.column(sum.functionName() + " [share]").setName("total_share");
        // 平均成本
        t.addColumns(t.doubleColumn("total_input").divide(t.doubleColumn("total_share")).setName("avg_net_worth"));

        Table realNetWord = realNetWorth();
        Table t2 = t.joinOn("code")
                .inner(realNetWord.select("code", "current_net_worth"));

        // 估算当前总价
        t2.addColumns(
                t2.doubleColumn("total_share")
                        .multiply(t2.doubleColumn("current_net_worth"))
                        .setName("total_value")
        );

        // syl
        t2.addColumns(
                // (当前总价-总投入)/总投入
                t2.doubleColumn("total_value")
                        .subtract(t2.doubleColumn("total_input"))
                        .divide(t2.doubleColumn("total_input"))
                        .setName("syl")
        );

        // 年化
        DoubleColumn annualized = DoubleColumn.create("annualized");
        for (int i = 0, count = t2.rowCount(); i < count; i++) {
            Row row = t2.row(i);
            Table recordsByCode = getRecords().where(getRecords().stringColumn("code").isEqualTo(row.getString("code")));
            DateColumn dates = recordsByCode.dateColumn("date");
            // fix：当天 15:00 以后可能获取到相关估值，若直接使用在会报错
            dates.append(LocalDate.now().plusDays(1));
            DoubleColumn amountes = recordsByCode.doubleColumn("amount").multiply(-1);
            amountes.append(row.getDouble("total_value"));
            annualized.append(IndexTool.syl(dates, amountes));
        }
        t2.addColumns(annualized);

        return t2;
    }

    public static void main(String[] args) throws Exception {
        Table table = Table.read().csv(MyAccount.class.getClassLoader().getResourceAsStream("account.csv"));
        PurchaseAnalysis analysis = new PurchaseAnalysis(table);
        System.out.println(analysis.getRecords().print());
        System.out.println(analysis.summarize().print());
    }
}
