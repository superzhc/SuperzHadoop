package com.github.superzhc.common.faker.demo;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * @author superz
 * @create 2021/10/24 16:53
 */
public class ExpressionMain {
    public static void main(String[] args) {
        Faker faker = Faker.instance(new Locale("zh-CN"));

        System.out.println("---------------------------Common------------------------------");
        // 从 男、女 可选项中选择一个
        System.out.println(faker.expression("#{regexify '(男|女){1}'}"));
        // 生成数字
        System.out.println(faker.expression("#{numerify 'AA##BB'}"));
        // 生成字母
        System.out.println(faker.expression("#{letterify '1234??'}"));
        // 即可生成数字也可生成字母
        System.out.println(faker.expression("#{bothify '1?5?D#F#'}"));
        // 多个表达式
        System.out.println(faker.expression("#{regexify '(GET|POST|PUT|DELETE|HEADER|PATCH){1}'} #{regexify '(search|login|prod|cart|order){1}'}"));

        /* Ancient */
        System.out.println("---------------------------Ancient------------------------------");
        System.out.println(faker.expression("#{Ancient.hero}"));
        System.out.println(faker.expression("#{Ancient.god}"));
        System.out.println(faker.expression("#{Ancient.hero}"));
        System.out.println(faker.expression("#{Ancient.primordial}"));
        System.out.println(faker.expression("#{Ancient.titan}"));

        /* App */
        System.out.println("----------------------------App-----------------------------");
        System.out.println(faker.expression("#{App.name}"));
        System.out.println(faker.expression("#{App.version}"));
        System.out.println(faker.expression("#{App.author}"));

        /* Aviation */
        System.out.println("----------------------------Aviation-----------------------------");
        System.out.println(faker.expression("#{Aviation.aircraft}"));
        System.out.println(faker.expression("#{Aviation.airport}"));
        System.out.println(faker.expression("#{Aviation.METAR}"));

        /* Name */
        System.out.println("----------------------------Name-----------------------------");
        System.out.println(faker.expression("#{Name.name }"));
        System.out.println(faker.expression("#{Name.nameWithMiddle}"));
        System.out.println(faker.expression("#{Name.fullName}"));
        System.out.println(faker.expression("#{Name.lastName}"));
        System.out.println(faker.expression("#{Name.firstName}"));
        System.out.println(faker.expression("#{Name.username}"));
        System.out.println(faker.expression("#{Name.title}"));

        /* Number */
        System.out.println("-----------------------------Number----------------------------");
        System.out.println(faker.expression("#{Number.randomDigit}"));
        System.out.println(faker.expression("#{Number.randomDigitNotZero}"));
        System.out.println(faker.expression("#{Number.numberBetween '1','5'}"));
        System.out.println(faker.expression("#{Number.randomNumber '1','true'}"));
        System.out.println(faker.expression("#{Number.randomNumber}"));
        System.out.println(faker.expression("#{Number.randomDouble '3','1','10'}"));
        //System.out.println(faker.expression("#{Number.decimalBetween '1','10'}"));//×
        System.out.println(faker.expression("#{Number.digits '10'}"));
        //System.out.println(faker.expression("#{Number.digits}"));//×
        //System.out.println(faker.expression("#{number_between '1','5'}"));//×

        /* Internet */
        System.out.println("-------------------------------Internet--------------------------");
        System.out.println(faker.expression("#{Internet.emailAddress}"));
        System.out.println(faker.expression("#{Internet.safeEmailAddress}"));
        System.out.println(faker.expression("#{Internet.url}"));
        System.out.println(faker.expression("#{Internet.macAddress}"));
        System.out.println(faker.expression("#{Internet.ipV4Address}"));
        System.out.println(faker.expression("#{Internet.ipV6Address}"));
        System.out.println(faker.expression("#{Internet.userAgentAny}"));

        /* PhoneNumber */
        System.out.println("--------------------------------PhoneNumber-------------------------");
        System.out.println(faker.expression("#{PhoneNumber.cellPhone}"));
        System.out.println(faker.expression("#{PhoneNumber.phoneNumber}"));
        System.out.println(faker.expression("#{PhoneNumber.subscriberNumber}"));
        System.out.println(faker.expression("#{PhoneNumber.subscriberNumber '6'}"));

        /* Bool */
        System.out.println("---------------------------------Bool------------------------");
        System.out.println(faker.expression("#{Bool.bool}"));

        /* DateAndTime */
        System.out.println("----------------------------------Date-----------------------");
        //System.out.println(faker.expression("#{DateAndTime.birthday}"));//×
        //System.out.println(faker.expression("#{DateAndTime.between '2021-10-20T00:00:00','2021-10-21T00:00:00'}"));//×
        //System.out.println(faker.expression("#{DateAndTime.past '5','10','SECONDS'}"));//×
        System.out.println(faker.expression("#{date.birthday}"));
        //System.out.println(faker.expression("#{date.between '2021-10-20T00:00:00','2021-10-21T00:00:00'}"));//×
        System.out.println(faker.expression("#{date.past '15','5','SECONDS'}"));
        System.out.println(faker.expression("#{date.past '15','SECONDS'}"));
        System.out.println(faker.expression("#{date.future '15','5','SECONDS'}"));
        System.out.println(faker.expression("#{date.future '5','SECONDS'}"));

        /* Code */
        System.out.println("---------------------------------Code------------------------");
        System.out.println(faker.expression("#{Code.imei}"));

        //System.out.println(faker.expression("#{}"));
    }
}
