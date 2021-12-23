package com.github.superzhc.data.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubRepoPageProcessor implements PageProcessor {

    // 设置抓取网站的相关配置，包括编码、抓取间隔、重试次数等等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    /**
     * process 是定制爬虫逻辑的核心接口
     * @param page
     */
    @Override
    public void process(Page page) {
        // 定义如何抽取页面信息，并保存下来
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name") == null) {
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        // 从页面发现后续的url地址来抓取
        // page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        Spider.create(new GithubRepoPageProcessor())
                // 从什么开始抓取
                .addUrl("https://github.com/code4craft")
                .thread(5)
                // 启动爬虫
                .run();
    }
}
