package com.github.superzhc.common.faker;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * 地球
 * @author superz
 * @create 2021/3/26 18:27
 */
public class Globe {
    private static final String LONGITUDE_PATTERN="[\\-\\+]?(0(\\.\\d{1,10})?|([1-9](\\d)?)(\\.\\d{1,10})?|1[0-7]\\d{1}(\\.\\d{1,10})?|180\\.0{1,10})";
    private static final String LATITUDE_PATTERN="[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,10})?|90(\\.0{1,10})?)";

    private final Faker faker;

    public Globe(Faker faker){
        this.faker=faker;
    }

    /**
     * 经度，见 Address
     * @return
     */
    @Deprecated
    public String longitude(){
        return faker.regexify(LONGITUDE_PATTERN);
    }

    /**
     * 维度，见 Address
     * @return
     */
    @Deprecated
    public String latitude(){
        return faker.regexify(LATITUDE_PATTERN);
    }
}
