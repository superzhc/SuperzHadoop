package com.github.superzhc.tablesaw.functions;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;

import java.math.BigDecimal;

/**
 * @author superz
 * @create 2022/5/20 0:11
 */
public class DoubleFunctions {
    public static DoubleColumn percentage(StringColumn data) {
        return data.map(
                str ->
                {
                    if (null == str || str.length() == 0 || "-".equals(str) || "--".equals(str)) {
                        return null;
                    }

                    String numStr = str.substring(0, str.length() - 1);
                    double d = new BigDecimal(numStr).multiply(new BigDecimal("0.01")).doubleValue();
                    return d;
                },
                name -> DoubleColumn.create(name + "[Percentage]")
        );
    }
}
