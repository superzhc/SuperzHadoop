package com.github.superzhc.data.article;

import com.github.superzhc.data.article.impl.JueJin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 文章采集器
 *
 * @author superz
 * @create 2021/12/28 14:04
 */
@Deprecated
public class ArticleCollector {
    public static void main(String[] args) throws Exception {
        String url = "https://juejin.cn/post/6844903682232827912";
//        Document doc = Jsoup.connect(url).get();
//        JueJin jueJin = new JueJin(doc);
        JueJin jueJin = new JueJin(url);
        System.out.println(jueJin.title());
        System.out.println(jueJin.author());
        System.out.println(jueJin.date());
        System.out.println(jueJin.content());
    }
}
