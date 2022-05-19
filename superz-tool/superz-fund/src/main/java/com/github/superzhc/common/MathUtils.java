package com.github.superzhc.common;

import org.apache.commons.math3.stat.StatUtils;

/**
 * @author superz
 * @create 2022/5/19 9:33
 **/
public class MathUtils {
    private MathUtils() {
    }

    public static double position(double[] array, double value) {
        if (null == array || array.length == 0) {
            return 1.0;
        }

        int lessCount = 0;
        for (double item : array) {
            if (item < value) {
                lessCount++;
            }
        }

        return lessCount / (1.0 * array.length);
    }
}
