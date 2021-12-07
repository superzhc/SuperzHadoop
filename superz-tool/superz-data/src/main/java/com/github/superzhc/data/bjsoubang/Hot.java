package com.github.superzhc.data.bjsoubang;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 搜榜-热榜数据
 *
 * @author superz
 * @create 2021/12/5 23:08
 */
public class Hot {
    private static final String URL = "https://www.bjsoubang.com/api/getChannelData";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    /**
     * 豆瓣新片榜
     *
     * @return
     */
    public String doubanMovie() {
        return execute(16);
    }

    public String doubanBook() {
        return execute(13);
    }

    /**
     * 一周热门虚构类图书
     *
     * @return
     */
    public String doubanWeekHotBook() {
        // note：17、18参数都可以
        return execute(17);
    }

    /**
     * 豆瓣一周口碑榜
     *
     * @return
     */
    public String doubanWeekPraise() {
        return execute(19);
    }

    /**
     * 知乎热搜榜
     *
     * @return
     */
    public String zhihuHotSearch() {
        return execute(10);
    }

    /**
     * 知乎热榜
     *
     * @return
     */
    public String zhihuHot() {
        return execute(2);
    }

    /**
     * 微信热词榜
     *
     * @return
     */
    public String weixinHotWord() {
        return execute(6);
    }

    /**
     * 微信热门榜
     *
     * @return
     */
    public String weixinHot() {
        return execute(1);
    }

    /**
     * 微博热搜榜
     *
     * @return
     */
    @Deprecated
    public String weiboHotSearch() {
        return execute(4);
    }

    /**
     * 微博新时代榜
     *
     * @return
     */
    @Deprecated
    public String weiboNewEra() {
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
    public String baiduTodayHot() {
        return execute(12);
    }

    /**
     * 百度百科热词榜
     *
     * @return
     */
    public String baiduHotWord() {
        return execute(9);
    }

    private String execute(Integer channelId) {
        String url = URL + "?channel_id=" + channelId;
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return response.message();
            }

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "请求异常，" + e.getMessage();
        }
    }

    public static void main(String[] args) {
        Hot hot = new Hot();
        // System.out.println(hot.baiduHot());
        System.out.println(hot.baiduHotWord());
        // System.out.println(hot.baiduTodayHot());
        System.out.println(hot.doubanMovie());
        System.out.println(hot.doubanWeekPraise());
        System.out.println(hot.zhihuHot());
        System.out.println(hot.zhihuHotSearch());
        System.out.println(hot.weixinHot());
        System.out.println(hot.weixinHotWord());
        // System.out.println(hot.weiboHotSearch());
        // System.out.println(hot.weiboNewEra());
    }
}
