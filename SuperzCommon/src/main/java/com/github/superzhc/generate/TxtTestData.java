package com.github.superzhc.generate;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * 2020年11月03日 superz add
 */
public class TxtTestData extends GenerateFileTestData
{
    private String filepath;
    private int partition;
    private Supplier<String> supplier;

    public TxtTestData(String filepath) {
        this(filepath, 1);
    }

    public TxtTestData(String filepath, int partition) {
        this.filepath = filepath;
        this.partition = partition;
    }

    @Override
    public String path() {
        return filepath;
    }

    @Override
    public int partitions() {
        return partition;
    }

    @Override
    public String suffix() {
        return "txt";
    }

    @Override
    public byte[] generateContent(Supplier<String> supplier, long quantity) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < quantity; i++) {
            content.append(supplier.get());
        }
        return content.toString().getBytes();
    }

    public static void main(String[] args) {
        GenerateTestData testData = new TxtTestData("D:\\data\\che300", 3);
        Faker faker = new Faker(Locale.CHINA);
        testData.generate(() -> {
            String name = faker.name().name();
            String sex = faker.options().option("男", "女");
            int age = faker.number().numberBetween(18, 60);
            String married = faker.options().option("已婚", "未婚");// 是否结婚
            Address address = faker.address();
            String city = address.cityName();
            String area = address.streetName();
            String brand = faker.options().option("奔驰", "宝马", "奥迪", "雷克萨斯", "蔚来", "特斯拉");// 品牌
            String type = faker.options().option("轿车", "SUV", "商务车");
            String s = String.format("%s,%s,%d,%s,%s,%s,%s,%s\r\n", name, sex, age, married, city, area, brand, type);
            return s;
        }, 100000);
        System.out.println("测试数据生成结束");
    }
}
