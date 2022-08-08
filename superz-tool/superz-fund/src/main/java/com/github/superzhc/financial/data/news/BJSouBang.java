package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/8 14:06
 **/
public class BJSouBang {
    /**
     * 豆瓣新片榜
     *
     * @return
     */
    public static Table doubanMovie() {
        return execute(16);
    }

    public static Table doubanBook() {
        return execute(13);
    }

    /**
     * 一周热门虚构类图书
     *
     * @return
     */
    public static Table doubanWeekHotBook() {
        // note：17、18参数都可以
        return execute(17);
    }

    /**
     * 豆瓣一周口碑榜
     *
     * @return
     */
    public static Table doubanWeekPraise() {
        return execute(19);
    }

    /**
     * 知乎热搜榜
     *
     * @return
     */
    public static Table zhihuHotSearch() {
        return execute(10);
    }

    /**
     * 知乎热榜
     *
     * @return
     */
    public static Table zhihuHot() {
        return execute(2);
    }

    /**
     * 微信热词榜
     *
     * @return
     */
    public static Table weixinHotWord() {
        return execute(6);
    }

    /**
     * 微信热门榜
     *
     * @return
     */
    public static Table weixinHot() {
        return execute(1);
    }

    /**
     * 微博热搜榜
     *
     * @return
     */
    @Deprecated
    public static Table weiboHotSearch() {
        return execute(4);
    }

    /**
     * 微博新时代榜
     *
     * @return
     */
    @Deprecated
    public static Table weiboNewEra() {
        return execute(5);
    }

    /**
     * 百度实时热点榜
     *
     * 2021年12月5日 不可用接口
     *
     * @return
     */
//    public String baiduHot() {
//        return execute(3);
//    }

    /**
     * 百度今日热点榜
     *
     * @return
     */
    @Deprecated
    public static Table baiduTodayHot() {
        return execute(12);
    }

    /**
     * 百度百科热词榜
     *
     * @return
     */
    public static Table baiduHotWord() {
        return execute(9);
    }

    public static Table execute(int channelId) {
        String url = "https://www.bjsoubang.com/api/getChannelData";

        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", channelId);

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "info", "data");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = weixinHotWord();

        System.out.println(table.printAll());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
