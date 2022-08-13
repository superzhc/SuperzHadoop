package com.github.superzhc.financial.data.news;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;
import java.util.List;

/**
 * @author superz
 * @create 2022/8/13 17:32
 **/
public class NewsMain {
    public static void main(String[] args) throws Exception {
        String rss = "https://rsshub.v2fy.com/telegram/channel/zhaoolee_pi";

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(rss)));
        System.out.println(feed.getTitle());

        // 得到Rss新闻中子项列表
        List<SyndEntry> entries = feed.getEntries();
        // 循环得到每个子项信息
        for (int j = 0; j <= 0; j++) {
            SyndEntry entry = (SyndEntry) entries.get(j);
            SyndContent description = entry.getDescription();
            System.out.println("====================");
            System.out.println("SyndEntry对象：");
            System.out.println(entry.toString());
            System.out.println("SyndContent对象：");
            System.out.println(description.toString());
        }
    }
}
