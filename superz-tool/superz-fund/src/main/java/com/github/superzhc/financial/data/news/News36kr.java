package com.github.superzhc.financial.data.news;

import com.github.superzhc.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/8/15 0:35
 */
public class News36kr {
    private static final Logger log = LoggerFactory.getLogger(News36kr.class);

    // private static final String pattern = "\"itemList\":(\\[.*?\\])";

    /**
     * 最新资讯
     *
     * @return
     */
    public static Table latest() {
        return execute("web_news");
    }

    public static Table recommend() {
        return execute("web_recommend");
    }

    /**
     * 创投
     *
     * @return
     */
    public static Table vc() {
        return execute("contact");
    }

    public static Table car() {
        return execute("travel");
    }

    public static Table technology() {
        return execute("technology");
    }

    /**
     * 企服
     *
     * @return
     */
    public static Table enterpriseService() {
        return execute("enterpriseservice");
    }

    /**
     * 创新
     *
     * @return
     */
    public static Table innovate() {
        return execute("innovate");
    }

    /**
     * 房产
     *
     * @return
     */
    public static Table realEstate() {
        return execute("real_estate");
    }

    public static Table life() {
        return execute("happy_life");
    }

    public static Table other() {
        return execute("other");
    }

    /**
     * 财经
     *
     * @return
     */
    public static Table finance() {
        return execute("ccs");
    }

    private static Table execute(String category) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.news.News36kr.information(category));
        return table;
    }

    public static void main(String[] args) {
        Table table = other();

        if (null == table) {
            System.out.println("数据为空");
            return;
        }

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
