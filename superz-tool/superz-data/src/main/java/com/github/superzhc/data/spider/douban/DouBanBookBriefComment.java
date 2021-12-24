package com.github.superzhc.data.spider.douban;

import com.github.superzhc.data.spider.DBPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.UUID;

/**
 * 短评
 *
 * @author superz
 * @create 2021/12/24 14:31
 */
public class DouBanBookBriefComment implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        url = url.substring("https://book.douban.com/subject/".length());
        String bookId = url.substring(0, url.indexOf("/"));

        Html html = page.getHtml();
        List<Selectable> comments = html.css("#comments > div.comment-list.new_score > ul > li.comment-item").nodes();
        for (Selectable comment : comments) {
            String cid = comment.xpath("//li/@data-cid").get();
            String username = comment.css("div.avatar > a", "title").get();
            String commentTime = comment.css("div.comment > h3 > span.comment-info > span.comment-time", "text").get();
            String commentContent = comment.css("div.comment > p > span", "text").get();

            DBPipeline.ResultItemValue item = new DBPipeline.ResultItemValue();
            item.put("book_id", bookId);
            item.put("cid", cid);
            item.put("username", username);
            item.put("dt", commentTime);
            item.put("content", commentContent);
            page.putField(UUID.randomUUID().toString(), item);
        }

        List<String> next = page.getHtml().xpath("//*[@id=\"paginator\"]/a").links().all();
        page.addTargetRequests(next);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        Spider.create(new DouBanBookBriefComment())
                .addUrl("https://book.douban.com/subject/1084336/comments/?status=P&sort=new_score&percent_type=&start=220&limit=20")
                .addPipeline(new DBPipeline(url, username, password, "douban_book_brief_comment"))
                .run();
    }
}
