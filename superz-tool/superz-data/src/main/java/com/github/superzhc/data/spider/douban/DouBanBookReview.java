package com.github.superzhc.data.spider.douban;

import com.github.superzhc.data.spider.DBPipeline;
import com.github.superzhc.data.spider.proxy.ProxyPool;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.UUID;

/**
 * 书评
 *
 * @author superz
 * @create 2021/12/24 15:26
 */
public class DouBanBookReview implements PageProcessor {
    private Site site = Site.me()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
            .setRetryTimes(3).setSleepTime(1000);


    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        url = url.substring("https://book.douban.com/subject/".length());
        String bookId = url.substring(0, url.indexOf("/"));

        Html html = page.getHtml();

        List<Selectable> nodes = html.xpath("//*[@id=\"content\"]/div/div[@class=\"article\"]/div[@class=\"review-list  \"]/div").nodes();
        for (Selectable node : nodes) {
            String cid = node.xpath("//div/@data-cid").get();
            String username = node.xpath("//div[@class=\"main review-item\"]/header/a[@class=\"name\"]/text()").get();
            String dt = node.xpath("//div[@class=\"main review-item\"]/header/span[@class=\"main-meta\"]/text()").get();
            String reviewUrl = node.xpath("//div[@class=\"main review-item\"]/div[@class=\"main-bd\"]/h2/a/@href").get();
            String reviewTitle = node.xpath("//div[@class=\"main review-item\"]/div[@class=\"main-bd\"]/h2/a/text()").get();
            String reviewShortContent = node.xpath("//div[@class=\"main review-item\"]/div[@class=\"main-bd\"]/div[@class=\"review-short\"]/div[@class=\"short-content\"]/text()").get();

            DBPipeline.ResultItemValue item = new DBPipeline.ResultItemValue();
            item.put("book_id", bookId);
            item.put("cid", cid);
            item.put("username", username);
            item.put("dt", dt);
            item.put("url", reviewUrl);
            item.put("title", reviewTitle);
            item.put("short_content", reviewShortContent);
            page.putField(UUID.randomUUID().toString(), item);
        }

        String next = html.xpath("//*[@id=\"content\"]/div/div[1]/div[2]/span[4]/a").links().get();
        page.addTargetRequest(next);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        Spider.create(new DouBanBookReview())
                .addUrl("https://book.douban.com/subject/3183775/reviews")
                .setDownloader(ProxyPool.getInstance().getHttpClientDownloader())
                .addPipeline(new DBPipeline(url, username, password, "douban_book_review"))
                .run();
    }
}
