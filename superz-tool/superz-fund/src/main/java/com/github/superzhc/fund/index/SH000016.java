package com.github.superzhc.fund.index;

import com.github.superzhc.fund.data.index.CSIndex;
import tech.tablesaw.api.Table;

/**
 * 上证50
 *
 * @author superz
 * @create 2022/4/25 16:28
 **/
public class SH000016 {
    private static final String CODE = "000016.SH";

    public static void main(String[] args) {
        Table table = CSIndex.index(CODE);
        System.out.println(table.print());
        System.out.println(table.structure().printAll());
    }
}
