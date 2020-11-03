package com.github.superzhc.generate.javafaker;

import com.github.javafaker.Faker;

/**
 * 2020年11月03日 superz add
 */
@Deprecated
public class Sex
{
    private final Faker faker;

    public Sex(Faker faker) {
        this.faker = faker;
    }

    public String chinese() {
        return faker.options().option("男", "女");
    }
}
