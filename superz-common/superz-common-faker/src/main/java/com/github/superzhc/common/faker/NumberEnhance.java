package com.github.superzhc.common.faker;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;

import java.math.BigDecimal;

/**
 * @author superz
 * @create 2021/3/30 14:23
 */
public class NumberEnhance extends Number {

    protected NumberEnhance(Faker faker) {
        super(faker);
    }

    public float randomFloat(int maxNumberOfDecimals, int min, int max) {
        return randomFloat(maxNumberOfDecimals, (long) min, (long) max);
    }

    /**
     * Returns a random double
     *
     * @param maxNumberOfDecimals maximum number of places
     * @param min                 minimum value
     * @param max                 maximum value
     */
    public float randomFloat(int maxNumberOfDecimals, long min, long max) {
        return ((Double) randomDouble(maxNumberOfDecimals, min, max)).floatValue();
    }
}
