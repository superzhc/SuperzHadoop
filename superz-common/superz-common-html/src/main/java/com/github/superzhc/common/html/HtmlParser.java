package com.github.superzhc.common.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;


/**
 * @author superz
 * @create 2022/6/23 10:06
 **/
public class HtmlParser {
    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    private Document document;
    private JXDocument jxDocument;

    public HtmlParser(Document document) {
        this.document = document;
        this.jxDocument=JXDocument.create(document);
    }

    public static HtmlParser parse(String html) {
        Document document = Jsoup.parse(html);
        return new HtmlParser(document);
    }

    public static HtmlParser request(HttpRequest httpRequest) {
        String html = httpRequest.body();
        return parse(html);
    }

    public static HtmlParser file(String path) {
        return file(path, DEFAULT_CHARSET);
    }

    public static HtmlParser file(String path, String charset) {
        try {
            Document document = Jsoup.parse(new File(path), charset);
            return new HtmlParser(document);
        } catch (IOException e) {
            log.error("Html文件[{}]解析失败", path);
            throw new RuntimeException(e);
        }
    }

    public static HtmlParser browser(String url, Map<String, String> headers) {
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
            // 谷歌浏览器的agent，这个固定下来
            request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36");
            if (null != headers) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setAdditionalHeader(header.getKey(), header.getValue());
                }
            }
            HtmlPage page = client.getPage(request);

            //为了获取js执行的数据 线程开始沉睡等待
            Thread.sleep(1000 * 3);

            return parse(page.asXml());
        } catch (Exception e) {
            log.error("模拟浏览器渲染Html进行解析异常", e);
            throw new RuntimeException(e);
        }
    }
}
