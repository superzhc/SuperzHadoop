package com.github.superzhc.data.other;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 搜榜-热榜数据
 *
 * @author superz
 * @create 2022/9/7 23:30
 */
public class BJSouBang {
    /**
     * 豆瓣新片榜
     *
     * @return
     */
    public static List<Map<String, String>> doubanMovie() {
        return execute(16);
    }

    public static List<Map<String, String>> doubanBook() {
        return execute(13);
    }

    /**
     * 一周热门虚构类图书
     *
     * @return
     */
    public static List<Map<String, String>> doubanWeekHotBook() {
        // note：17、18参数都可以
        return execute(17);
    }

    /**
     * 豆瓣一周口碑榜
     *
     * @return
     */
    public static List<Map<String, String>> doubanWeekPraise() {
        return execute(19);
    }

    /**
     * 知乎热搜榜
     *
     * @return
     */
    public static List<Map<String, String>> zhihuHotSearch() {
        return execute(10);
    }

    /**
     * 知乎热榜
     *
     * @return
     */
    public static List<Map<String, String>> zhihuHot() {
        return execute(2);
    }

    /**
     * 微信热词榜
     *
     * @return
     */
    public static List<Map<String, String>> weixinHotWord() {
        return execute(6);
    }

    /**
     * 微信热门榜
     *
     * @return
     */
    public static List<Map<String, String>> weixinHot() {
        return execute(1);
    }

    /**
     * 微博热搜榜
     *
     * @return
     */
    @Deprecated
    public static List<Map<String, String>> weiboHotSearch() {
        return execute(4);
    }

    /**
     * 微博新时代榜
     *
     * @return
     */
    @Deprecated
    public static List<Map<String, String>> weiboNewEra() {
        return execute(5);
    }

    /**
     * 百度实时热点榜
     *
     * 2021年12月5日 不可用接口
     *
     * @return
     */
//    public List<Map<String, String>> baiduHot() {
//        return execute(3);
//    }

    /**
     * 百度今日热点榜
     *
     * @return
     */
    @Deprecated
    public static List<Map<String, String>> baiduTodayHot() {
        return execute(12);
    }

    /**
     * 百度百科热词榜
     *
     * @return
     */
    public static List<Map<String, String>> baiduHotWord() {
        return execute(9);
    }

    public static List<Map<String, String>> execute(Integer channelId) {
        String url = String.format("https://www.bjsoubang.com/api/getChannelData?channel_id=%d", channelId);
        String result = HttpRequest.get(url).body();
        JsonNode data = JsonUtils.json(result, "info", "data");
        Map<String, String>[] maps = JsonUtils.objectArray2Map(data);

        return Arrays.asList(maps);
    }

    public static void main(String[] args) {
        System.out.println(BJSouBang.weixinHot());
    }
}
