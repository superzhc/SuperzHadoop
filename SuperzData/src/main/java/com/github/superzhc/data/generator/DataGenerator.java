package com.github.superzhc.data.generator;

import com.github.superzhc.data.faker.FakerExt;

import java.util.Locale;

/**
 * @author superz
 */
public abstract class DataGenerator<T> {
    protected FakerExt faker;

    public DataGenerator() {
        this.faker = new FakerExt();
    }

    public DataGenerator(String locale) {
        this.faker = new FakerExt(new Locale(locale));
    }

    public DataGenerator(Locale locale) {
        this.faker = new FakerExt(locale);
    }

    public abstract T generate();

    public String convert2String() {
        return generate().toString();
    }
}
