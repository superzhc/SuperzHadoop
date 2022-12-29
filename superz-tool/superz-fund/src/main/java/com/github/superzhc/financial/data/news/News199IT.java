package com.github.superzhc.financial.data.news;

import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/8/16 23:31
 */
public class News199IT {
    /**
     * 报告
     *
     * @return
     */
    public static Table report() {
        return category("report");
    }

    /**
     * 医疗健康
     *
     * @return
     */
    public static Table healthTech() {
        return category("emerging/health-tech");
    }

    public static Table newEnergy() {
        return category("emerging/新能源");
    }

    /**
     * 新基建
     *
     * @return
     */
    public static Table infrastructure() {
        return category("emerging/xinjijian");
    }

    public static Table iot() {
        return category("emerging/物联网");
    }

    public static Table industry() {
        return category("emerging/工业4-0");
    }

    public static Table dataMing() {
        return category("dataindustry/data-mining");
    }

    public static Table bigdata() {
        return tag("大数据");
    }

    public static Table data() {
        return tag("数据早报");
    }

    public static Table category(String path) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.report.News199IT.category(path));
        return table;
    }

    public static Table tag(String path) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.report.News199IT.tag(path));
        return table;
    }

    public static void main(String[] args) {
        String tag = "数据早报";
        Table table = industry();//tag(tag);
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
