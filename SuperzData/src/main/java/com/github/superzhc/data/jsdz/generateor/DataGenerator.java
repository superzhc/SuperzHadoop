package com.github.superzhc.data.jsdz.generateor;

import com.github.javafaker.Faker;

import java.util.Locale;

public abstract class DataGenerator<T> {
    protected Faker faker;

    public DataGenerator() {
        this.faker = new Faker();
    }

    public DataGenerator(String locale) {
        this.faker = new Faker(new Locale(locale));
    }

    public DataGenerator(Locale locale) {
        this.faker = new Faker(locale);
    }

    public abstract T generate();

    public String convert2String() {
        return generate().toString();
    }
}
