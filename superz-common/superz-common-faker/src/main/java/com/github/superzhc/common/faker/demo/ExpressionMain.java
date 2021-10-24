package com.github.superzhc.common.faker.demo;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * @author superz
 * @create 2021/10/24 16:53
 */
public class ExpressionMain {
    public static void main(String[] args) {
        Faker faker=Faker.instance(new Locale("zh-CN"));

        System.out.println(faker.expression("#{Name.name}"));
        // 从 男、女 可选项中选择一个
        System.out.println(faker.expression("#{regexify '(男|女){1}'}"));
        // 生成数字
        System.out.println(faker.expression("#{numerify 'AA##BB'}"));
        // 生成字母
        System.out.println(faker.expression("#{letterify '1234??'}"));
        // 即可生成数字也可生成字母
        System.out.println(faker.expression("#{bothify '1?5?D#F#'}"));
    }
}
