package com.github.superzhc.common.faker;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.Locale;
import java.util.Random;

/**
 * 扩展Faker的部分能力
 *
 * @author superz
 * @create 2021/3/26 16:50
 */
public class FakerEnhance extends Faker {
    private final Car car;
    private final Globe globe;
    private final NumberEnhance numberEnhance;

    public FakerEnhance() {
        this(Locale.ENGLISH);
    }

    public FakerEnhance(Locale locale) {
        this(locale, (Random) null);
    }

    public FakerEnhance(Random random) {
        this(Locale.ENGLISH, random);
    }

    public FakerEnhance(Locale locale, Random random) {
        this(locale, new RandomService(random));
    }

    public FakerEnhance(Locale locale, RandomService randomService) {
        this(new FakeValuesService(locale, randomService), randomService);
    }

    public FakerEnhance(FakeValuesService fakeValuesService, RandomService random) {
        super(fakeValuesService, random);

        this.car = new Car(this);
        this.globe = new Globe(this);
        this.numberEnhance = new NumberEnhance(this);
    }

    public Car car() {
        return this.car;
    }

    public Globe globe() {
        return this.globe;
    }

    /**
     * 扩展Number
     *
     * @return
     */
    public NumberEnhance numberEnhance() {
        return this.numberEnhance;
    }
}
