package com.github.superzhc.fund;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/4/24 18:00
 **/
public class InvestBackTest {
    public static void main(String[] args) throws Exception {
        int weeks = 52;
        LocalDate day = LocalDate.of(2022, 4, 25);
//        LocalDate lastYearDay = day.minusDays(7 * weeks);

        String code = "160119";
        Double amount = 200.0;


        Table table = Table.create(
                DateColumn.create("date"),
                StringColumn.create("code"),
                DoubleColumn.create("amount")
        );

        for (int i = weeks; i > 0; i--) {
            LocalDate date = day.minusDays(7 * i);
            table.dateColumn("date").appendObj(date);
            table.stringColumn("code").append(code);
            table.doubleColumn("amount").append(amount);
        }

        //System.out.println(table.print());
        String path = InvestBackTest.class.getClassLoader().getResource("week.csv").getPath();
        System.out.println(path);
        table.write().csv(path);
    }
}
