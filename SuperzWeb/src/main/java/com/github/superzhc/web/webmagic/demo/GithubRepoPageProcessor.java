package com.github.superzhc.web.webmagic.demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 2020年09月23日 superz add
 */
public class GithubRepoPageProcessor implements PageProcessor
{
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        /**
         * xpath(String xpath)	使用XPath选择	html.xpath("//div[@class='title']")
         * $(String selector)	使用Css选择器选择	html.$("div.title")
         * $(String selector,String attr)	使用Css选择器选择	html.$("div.title","text")
         * css(String selector)	功能同$()，使用Css选择器选择	html.css("div.title")
         * links()	选择所有链接	html.links()
         * regex(String regex)	使用正则表达式抽取	html.regex("\(.\*?)\")
         * regex(String regex,int group)	使用正则表达式抽取，并指定捕获组	html.regex("\(.\*?)\",1)
         * replace(String regex, String replacement)	替换内容	html.replace("\","")
         */
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name") == null) {
            // skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        // 部分三：从页面发现后续的url地址来抓取
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://github.com/code4craft")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
