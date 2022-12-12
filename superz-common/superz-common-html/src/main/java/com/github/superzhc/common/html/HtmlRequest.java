package com.github.superzhc.common.html;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author superz
 * @create 2022/10/25 23:54
 **/
public class HtmlRequest implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(HtmlRequest.class);

    private WebClient client = null;

    public HtmlRequest(WebClient client) {
        this.client = client;
    }

    public static HtmlRequest chrome() {
        return browser(BrowserVersion.CHROME);
    }

    public static HtmlRequest firefox() {
        return browser(BrowserVersion.FIREFOX);
    }

    public static HtmlRequest edge() {
        return browser(BrowserVersion.EDGE);
    }

    private static HtmlRequest browser(BrowserVersion browserVersion) {
        HtmlRequest request = new HtmlRequest(new WebClient(browserVersion));
        request.setCssEnabled(false);
        request.setThrowExceptionOnFailingStatusCode(false);
        request.setThrowExceptionOnScriptError(false);
        request.setAjaxController(new NicelyResynchronizingAjaxController());
        request.waitForBackgroundJavaScript(1000 * 3);
        return request;
    }

    public WebClient getClient() {
        return this.client;
    }

    public WebClientOptions getClientOptions() {
        return this.client.getOptions();
    }

    @Override
    public void close() throws IOException {
        if (null != client) {
            client.close();
        }
    }

    /**
     * 设置JS执行的超时时间
     *
     * @param timeoutMillis
     * @return
     */
    public HtmlRequest waitForBackgroundJavaScript(final long timeoutMillis) {
        getClient().waitForBackgroundJavaScript(timeoutMillis);
        return this;
    }

    public HtmlRequest setJavaScriptTimeout(final long timeout) {
        getClient().setJavaScriptTimeout(timeout);
        return this;
    }

    public HtmlRequest setAjaxController(final AjaxController newValue) {
        getClient().setAjaxController(newValue);
        return this;
    }

    /**
     * 启用JS解释器，默认为true
     *
     * @param enabled
     * @return
     */
    public HtmlRequest setJavaScriptEnabled(final boolean enabled) {
        getClientOptions().setJavaScriptEnabled(enabled);
        return this;
    }

    /**
     * 启用CSS解释器，默认为true
     *
     * @param enabled
     * @return
     */
    public HtmlRequest setCssEnabled(final boolean enabled) {
        getClientOptions().setCssEnabled(enabled);
        return this;
    }

    /**
     * js运行错误时，是否抛出异常
     *
     * @param enabled
     * @return
     */
    public HtmlRequest setThrowExceptionOnScriptError(final boolean enabled) {
        getClientOptions().setThrowExceptionOnScriptError(enabled);
        return this;
    }

    /**
     * 状态码错误时，是否抛出异常
     *
     * @param enabled
     * @return
     */
    public HtmlRequest setThrowExceptionOnFailingStatusCode(final boolean enabled) {
        getClientOptions().setThrowExceptionOnFailingStatusCode(enabled);
        return this;
    }

    public HtmlRequest setActiveXNative(final boolean allow) {
        getClientOptions().setActiveXNative(allow);
        return this;
    }

    /**
     * 设置“浏览器”的请求超时时间
     *
     * @param timeout
     * @return
     */
    public HtmlRequest setTimeout(final int timeout) {
        getClientOptions().setTimeout(timeout);
        return this;
    }

    public HtmlPage getPage(final CharSequence url) {
        return getPage(url, null);
    }

    public HtmlPage getPage(final CharSequence url, Map<String, String> headers) {
        try {
            log.debug("请求页面：{}", url);
            URL uri = new URL(url.toString());
            WebRequest request = new WebRequest(uri);
            if (null != headers && !headers.isEmpty()) {
                request.setAdditionalHeaders(headers);
            }

            HtmlPage page = client.getPage(request);
            return page;
        } catch (Exception e) {
            log.error("加载网页异常", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

    }
}
