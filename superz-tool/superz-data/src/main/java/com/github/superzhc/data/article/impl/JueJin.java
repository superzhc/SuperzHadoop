package com.github.superzhc.data.article.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

/**
 * juejin 将文章内容放到 js 中了，只有 js 渲染完成后才能通过 css、xpath 抓取需要的内容
 *
 * @author superz
 * @create 2021/12/28 14:21
 */
@Deprecated
public class JueJin {
    private Document document = null;

    public JueJin(Document document) {
        this.document = document;
    }

    /**
     * js运行报错，依旧没有执行完成，依旧获取不到内容
     *
     * @param url
     */
    public JueJin(String url) {
        try (WebClient client = new WebClient(BrowserVersion.CHROME)) {
            // 启用JS解释器，默认为true
            client.getOptions().setJavaScriptEnabled(true);
            // 禁用CSS
            client.getOptions().setCssEnabled(false);
            //js运行错误时，是否抛出异常
            client.getOptions().setThrowExceptionOnScriptError(false);
            //状态码错误时，是否抛出异常
            client.getOptions().setThrowExceptionOnFailingStatusCode(false);
            //是否允许使用ActiveX
            client.getOptions().setActiveXNative(false);
            //等待js时间
            client.waitForBackgroundJavaScript(60 * 1000);
            //设置Ajax异步处理控制器即启用Ajax支持
            client.setAjaxController(new NicelyResynchronizingAjaxController());
            //设置超时时间
            // client.getOptions().setTimeout(1000 * 30);
            //不跟踪抓取
            // client.getOptions().setDoNotTrackEnabled(false);

            WebRequest request = new WebRequest(new URL(url));
            request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
            HtmlPage page = client.getPage(request);

//            HtmlPage page = client.getPage(url);
            //为了获取js执行的数据 线程开始沉睡等待
            Thread.sleep(1000 * 10);
            document = Jsoup.parse(page.asXml());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String title() {
        return document.selectFirst("meta[itemprop=headline]").attr("content");
    }

    public String author() {
        return document.selectFirst("meta[itemprop=name]").attr("content");
    }

    public String content() {
        return document.selectFirst("div.article-content").html();
    }

    public String date() {
        return document.selectFirst("meta[itemprop=datePublished]").attr("content");
    }
}
