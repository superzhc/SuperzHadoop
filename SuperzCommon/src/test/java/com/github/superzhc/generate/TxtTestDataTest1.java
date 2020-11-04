package com.github.superzhc.generate;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.junit.Test;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2020年11月03日 superz add
 */
public class TxtTestDataTest1
{
//    @Test
    public void generate001() {
        System.out.println("测试数据生成开始");
        GenerateTestData testData = new TxtTestData("D:\\data\\xiamen\\peopleinfo");
        AtomicInteger serialNumber = new AtomicInteger(0);
        Faker faker = new Faker(Locale.CHINA);
        testData.generate(() -> {
            String sex = faker.options().option("F", "M");
            int height = faker.number().numberBetween(150, 200);
            return String.format("%d  %s  %s\r\n", serialNumber.addAndGet(1), sex, height);
        }, 100000);
        System.out.println("测试数据生成结束");
    }

    public void generate002(){
        System.out.println("测试数据生成开始");
        GenerateTestData testData = new TxtTestData("D:\\data\\practice\\");
        AtomicInteger serialNumber = new AtomicInteger(0);
        Faker faker = new Faker(Locale.CHINA);
        testData.generate(() -> {
            String sex = faker.options().option("F", "M");
            int height = faker.number().numberBetween(150, 200);
            return String.format("%d  %s  %s\r\n", serialNumber.addAndGet(1), sex, height);
        }, 100000);
        System.out.println("测试数据生成结束");
    }
}
