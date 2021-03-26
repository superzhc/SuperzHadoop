package com.github.superzhc.data.faker;

import com.github.javafaker.Faker;
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
public class FakerExt extends Faker {
    private final Car car;
    private final Globe globe;

    public FakerExt() {
        this(Locale.ENGLISH);
    }

    public FakerExt(Locale locale) {
        this(locale, (Random) null);
    }

    public FakerExt(Random random) {
        this(Locale.ENGLISH, random);
    }

    public FakerExt(Locale locale, Random random) {
        this(locale, new RandomService(random));
    }

    public FakerExt(Locale locale, RandomService randomService) {
        this(new FakeValuesService(locale, randomService), randomService);
    }

    public FakerExt(FakeValuesService fakeValuesService, RandomService random) {
        super(fakeValuesService, random);

        this.car = new Car(this);
        this.globe = new Globe(this);
    }

    public Car car() {
        return this.car;
    }

    public Globe globe() {
        return this.globe;
    }
}
