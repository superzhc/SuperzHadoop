package com.github.superzhc.generate.javafaker;

import com.github.javafaker.Faker;

/**
 * 2020年11月03日 superz add
 */
public class Age
{
    private final Faker faker;

    public Age(Faker faker) {
        this.faker = faker;
    }

    public int age() {
        return faker.number().numberBetween(0, 100);
    }

    public int child() {
        return faker.number().numberBetween(0, 7);
    }

    public int middle() {
        return faker.number().numberBetween(18, 60);
    }

    public int elder() {
        return faker.number().numberBetween(60, 100);
    }

    /**
     * 未成年人
     * @return
     */
    public int minor() {
        return faker.number().numberBetween(0, 18);
    }

    /**
     * 成年人
     * @return
     */
    public int adult() {
        return faker.number().numberBetween(18, 100);
    }
}
