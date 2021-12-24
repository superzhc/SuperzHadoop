package com.github.superzhc.data.spider.douban;

import com.github.superzhc.data.spider.DBPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author superz
 * @create 2021/12/23 18:57
 */
public class DouBanBook implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<String> links = html.links().regex("https://book.douban.com/tag/[\\u4e00-\\u9fa5_a-zA-Z0-9]{1,}").all();

        if ("https://book.douban.com/tag/".equals(page.getUrl().get())) {
            page.setSkip(true);
            for (int i = 1, len = links.size(); i < len; i++) {
                page.addTargetRequest(links.get(i));
            }
        } else {
            List<Selectable> subjects = html.$("#subject_list .subject-list>.subject-item").nodes();
            int index = 1;
            for (Selectable subject : subjects) {
                String itemImage = subject.css("div.pic > a > img", "href").get();
                String itemUrl = subject.css("div.info > h2 > a", "href").get();
                String itemName = subject.css("div.info > h2 > a", "title").get();
                // 示例：余华 / 作家出版社 / 2012-8-1 / 20.00元
                String pub = subject.css("div.info > div.pub", "text").get();
                String author = pub;
                if (pub.indexOf("/") != -1) {
                    author = pub.substring(0, pub.indexOf("/")).trim();
                }
                String ratingNum = subject.css("div.info > div.star.clearfix > span.rating_nums", "text").get();
                String introduction = subject.css("div.info > p", "text").get();

                DBPipeline.ResultItemValue item = new DBPipeline.ResultItemValue();
                item.put("name", itemName);
                item.put("url", itemUrl);
                item.put("author", author);
                item.put("image", itemImage);
                item.put("rating_num", ratingNum);
                item.put("introduction", introduction);

                page.putField(String.valueOf(index++), item);
            }

            String next = html.$("#subject_list > div.paginator > span.next > a").links().get();
            if (null != next && next.trim().length() > 0) {
                page.addTargetRequest(next);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        Spider.create(new DouBanBook())
                // 豆瓣图书标签
                .addUrl("https://book.douban.com/tag/互联网?start=1000&type=T")
                .addPipeline(new DBPipeline(url, username, password, "douban_book"))
                .run();
    }
}
