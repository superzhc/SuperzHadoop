package com.github.superzhc.data.news;

import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.common.ResultT;

/**
 * @author superz
 * @create 2022/1/10 18:25
 */
public class NewsMain extends HttpData {
    public static final String SINA_TEMPLATE_URL = "http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=43&spec=&type=&ch=03&k=&offset_page=%d&offset_num=0&num=%d&asc=&page=1&r=0.%d";
    public static final String SOHU_TEMPLATE_URL = "http://v2.sohu.com/public-api/feed?scene=CHANNEL&sceneId=15&page=%d&size=%d";
    public static final String XINHUANET_TEMPLATE_URL = "http://qc.wa.news.cn/nodeart/list?nid=11147664&pgnum=%d&cnt=%d&tp=1&orderby=1";

//    public void sina(Integer page, Integer size) {
//        String url = String.format(SINA_TEMPLATE_URL, page, size, System.currentTimeMillis());
//        ResultT resultT = get(url);
//        System.out.println(resultT);
//    }

    public void soHu(Integer page, Integer size) {
        String url = String.format(SOHU_TEMPLATE_URL, page, size);
        ResultT resultT = get(url);
        System.out.println(resultT);
    }

    public void xinHuaNet(Integer page, Integer size) {
        String url = String.format(XINHUANET_TEMPLATE_URL, page, size);
        ResultT resultT = get(url);
        System.out.println(resultT);
    }

    public static void main(String[] args) {
        NewsMain news = new NewsMain();
        // news.xinHuaNet(1, 200);
        // news.soHu(1, 100);
        // news.sina(1, 100);
    }
}
