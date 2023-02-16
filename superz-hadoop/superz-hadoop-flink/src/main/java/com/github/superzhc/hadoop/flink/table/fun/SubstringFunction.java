package com.github.superzhc.hadoop.flink.table.fun;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * @author superz
 * @create 2023/2/16 17:43
 **/
public class SubstringFunction extends ScalarFunction {
    public String eval(String str, Integer start, Integer end) {
        return str.substring(start, end);
    }
}
