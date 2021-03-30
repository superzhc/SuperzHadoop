package com.github.superzhc.data.generator;

import com.github.superzhc.data.faker.FakerExt;
import lombok.Data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 数据生成器
 *
 * @author superz
 */
public abstract class DataGenerator<T> {
    protected FakerExt faker;

    /**
     * 存储重复数据的容器
     */
    private Map<String, RepeatObject> repeatObjectContainer = new HashMap<>();

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

    /**
     * 生成重复的数据
     *
     * @param key
     * @param value
     * @param nums
     * @param <K>
     * @return
     */
    protected <K> K repeat(String key, K value, int nums) {
        RepeatObject repeatObject;

        if (!repeatObjectContainer.containsKey(key)) {
            repeatObject = new RepeatObject();
            repeatObject.setKey(key);
            repeatObject.setData(value);
            repeatObject.setCursor(1);
            repeatObjectContainer.put(key, repeatObject);
        } else {
            repeatObject = repeatObjectContainer.get(key);
            repeatObject.setCursor(repeatObject.getCursor() + 1);
        }

        K value2 = (K) repeatObject.getData();

        //判断重复数据是否已经用完了
        // 理论上使用等于就可以了，但使用大于等于可以有效的防止非法的次数，如0，负数等
        if (repeatObject.getCursor() >= nums) {
            repeatObjectContainer.remove(key);
        }

        return value2;
    }

    public String convert2String() {
        return generate().toString();
    }

    @Data
    private static class RepeatObject {
        private String key;
        private Object data;
        private Integer cursor = 0;
    }
}
