package com.github.superzhc.common.tablesaw.tutorial;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

/**
 * 开始使用 Tablesaw
 *
 * @author superz
 * @create 2021/9/7 14:46
 */
public class HelloTablesaw {
    public static void main(String[] args) {
        /* 创建列名为nc的一列表格 */
        double[] numbers = {1, 2, 3, 4};
        DoubleColumn nc = DoubleColumn.create("nc", numbers);
        System.out.println(nc.print());

        /* 获取指定行数据，注意：索引从0开始 */
        System.out.println(nc.get(2));

        /* 对一列数据进行操作 */
        DoubleColumn nc2 = nc.multiply(4);
        System.out.println(nc2.print());

        StringColumn s = StringColumn.create("sc", new String[]{"foo", "bar", "baz", "foobarbaz"});
        /*对数据进行操作*/
        StringColumn s2 = s.replaceFirst("foo", "xxx") // 替换开头
                .upperCase()//字母全部变成大写
                .padEnd(5, '*')//字符不足5个的时候，填充*符号
                .substring(1, 5)//截取字符串
                ;
        System.out.println(s.print());
        System.out.println(s2.print());

        /**
         * 表格
         */
        String[] animals = {"bear", "cat", "giraffe"};
        double[] cuteness = {90.1, 84.3, 99.7};

        Table cuteAnimals =
                Table.create("Cute Animals")
                        .addColumns(
                                StringColumn.create("Animal types", animals),
                                DoubleColumn.create("rating", cuteness));
        System.out.println(cuteAnimals.print());
    }
}
