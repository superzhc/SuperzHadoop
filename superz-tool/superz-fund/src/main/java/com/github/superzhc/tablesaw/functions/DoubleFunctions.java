package com.github.superzhc.tablesaw.functions;

import com.github.superzhc.common.MathUtils;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumericColumn;
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

    /**
     * 值在列中所处的百分位
     *
     * @param column
     * @param value
     * @return
     */
    public static Double position(NumericColumn<?> column, double value) {
        double[] values = removeMissing(column);

        return MathUtils.position(values, value);
    }

    private static double[] removeMissing(NumericColumn<?> column) {
        NumericColumn<?> numericColumn = (NumericColumn<?>) column.removeMissing();
        return numericColumn.asDoubleArray();
    }
}
