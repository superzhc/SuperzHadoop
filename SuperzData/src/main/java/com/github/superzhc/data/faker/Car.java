package com.github.superzhc.data.faker;

import com.github.javafaker.Faker;

import javax.print.attribute.standard.OrientationRequested;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2021/3/26 16:23
 */
public class Car {
    private static final String ORDINARY_LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
    private static final String NEW_ENERGY_LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF][A-HJ-NP-Z0-9][0-9]{4}))";
    private static final String LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z](([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([0-9]{5}[DF]))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
    private static final String[] LICENSE_PLATE_COLOR_PATTERN = {"蓝", "黄", "白", "绿", "黑"};

    private final Faker faker;

    public Car(Faker faker) {
        this.faker = faker;
    }

    /**
     * 车牌号（包含新能源车牌号）
     *
     * @return
     */
    public String licensePlate() {
        // 直接对包含两种规则的正则生成的号牌是有问题的
        //return this.faker.regexify(LICENSE_PLATE_PATTERN);
        if (this.faker.bool().bool()) {
            return ordinaryLicensePlate();
        } else {
            return newEnergyLicensePlate();
        }
    }

    /**
     * 普通车牌号
     *
     * @return
     */
    public String ordinaryLicensePlate() {
        return this.faker.regexify(ORDINARY_LICENSE_PLATE_PATTERN);
    }

    /**
     * 新能源车牌号
     *
     * @return
     */
    public String newEnergyLicensePlate() {
        return this.faker.regexify(NEW_ENERGY_LICENSE_PLATE_PATTERN);
    }

    /**
     * 车牌颜色
     *
     * @return
     */
    public String licensePlateColor() {
        return this.faker.options().option(LICENSE_PLATE_COLOR_PATTERN);
    }

    public static void main(String[] args) throws InterruptedException {
        for (; ; ) {
            System.out.println(new FakerExt().car().licensePlate());
            Thread.sleep(1000);
        }
    }
}
