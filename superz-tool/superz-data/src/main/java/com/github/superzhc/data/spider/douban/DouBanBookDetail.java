package com.github.superzhc.data.spider.douban;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 未测试
 *
 * @author superz
 * @create 2021/12/24 17:20
 */
public class DouBanBookDetail implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();

//        String name = html.xpath("//*[@id=\"wrapper\"]/h1/span/text()").get();
//        String author = html.xpath("//*[@id=\"info\"]/a/text()").get();
//        List<String> pub = html.xpath("//*[@id=\"info\"]/text()").all();
//        String name2 = html.xpath("//*[@id=\"info\"]/text()").get();
//        String pubYear = html.xpath("//*[@id=\"info\"]/text()").get();
//        String ISBN = html.xpath("//*[@id=\"info\"]/text()[8]").get();
//        List<String> content = html.xpath("//*[@id=\"link-report\"]/span[2]/div/div/p/").all();
//        List<String> authorInfo = html.xpath("//*[@id=\"content\"]/div/div[1]/div[3]/div[2]/div/div/p").all();
//
//        page.putField("name", name);
//        page.putField("author", author);
//        page.putField("pub", pub);
//        page.putField("name2", name2);
//        page.putField("pubYear", pubYear);
//        page.putField("ISBN", ISBN);
//        page.putField("content", content);
//        page.putField("authorInfo", authorInfo);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DouBanBookDetail())
                .addUrl("https://book.douban.com/subject/3183775/")
                .run();
    }
}
