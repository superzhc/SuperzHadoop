package com.github.superzhc.data.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author superz
 * @create 2021/12/23 18:57
 */
public class DouBanBook implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String bookName = html.xpath("//*[@id=\"wrapper\"]/h1/span/text()").toString();
        String bookAuthor = html.css("#info > span:nth-child(1) > a","text").get();
        String ISBN=html.xpath("/html/body/div[3]/div[3]/div/div[1]/div[1]/div[1]/div[1]/div[2]/text()[6]").get();
        page.putField("name", bookName);
        page.putField("author", bookAuthor);
        page.putField("ISBN",ISBN);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DouBanBook())
                // 从什么开始抓取
                .addUrl("https://book.douban.com/subject/35571598/")
                // 启动爬虫
                .run();
    }
}
