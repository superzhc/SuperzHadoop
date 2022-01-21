package com.github.superzhc.data.common;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/1/12 10:38
 */
public class HtmlData {
    private static final Logger log = LoggerFactory.getLogger(HtmlData.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 通过 URL 获取页面
     *
     * @param url
     * @return
     */
    public Doc get(String url) {
        return get(url, null);
    }

    public Doc get(String url, Map<String, String> headers) {
        try {
            log.debug("Request{method=GET,url=" + url + "}");

            Connection connection = Jsoup.connect(url);

            if (null != headers) {
                connection.headers(headers);
            }

            Document document = connection.get();
            return new Doc(document);
        } catch (IOException e) {
            log.debug("Response{code=500,message=获取页面异常,url=" + url + "}", e);
            return null;
        }
    }

    public Doc post(String url, Map<String, String> form) {
        return post(url, null, form);
    }

    public Doc post(String url, Map<String, String> headers, Map<String, String> form) {
        try {
            log.debug("Request{method=POST,url=" + url + "}");
            Connection connection = Jsoup.connect(url);

            if (null != headers) {
                connection.headers(headers);
            }

            if (null != form) {
                connection.data(form);
            }

            Document document = connection.post();
            return new Doc(document);
        } catch (IOException e) {
            log.error("获取页面异常", e);
            return null;
        }
    }

    public Doc post(String url, Map<String, String> headers, String json) {
        try {
            log.debug("Request{method=POST,url=" + url + "}");

            Connection connection = Jsoup.connect(url);

            if (null != headers) {
                connection.headers(headers);
            }
            // 不管是否存在文件头，post json这个是必须的
            connection.header("Content-Type", "application/json");

            connection.requestBody(json);

            Document document = connection.post();
            return new Doc(document);
        } catch (IOException e) {
            log.debug("Response{code=500,message=获取页面异常,url=" + url + "}", e);
            return null;
        }
    }

    /**
     * 模拟浏览器获取渲染后的页面
     * <p>
     * 注意：这个会有 3s 的渲染时间
     *
     * @param url
     * @return
     */
    public Doc browser(String url) {
        return browser(url, null);
    }

    public Doc browser(String url, Map<String, String> headers) {
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

            /* 没啥效果
            boolean isHttps = url.startsWith("http://");
            String str = url.substring(isHttps ? 8 : 7);
            String baseUri = (isHttps ? "https://" : "http://") + str.substring(0, (str.indexOf("/") == -1 ? str.length() : str.indexOf("/")));
            return text(page.asXml(), baseUri);*/
            return text(page.asXml());
        } catch (Exception e) {
            log.error("模拟浏览器解析异常", e);
            return null;
        }
    }

    /**
     * 读取 HTML 文件
     *
     * @param path
     * @return
     */
    public Doc file(String path) {
        return file(path, DEFAULT_CHARSET);
    }

    /**
     * 读取 HTML 文件
     *
     * @param path
     * @param charset
     * @return
     */
    public Doc file(String path, String charset) {
        try {
            Document document = Jsoup.parse(new File(path), charset);
            return new Doc(document);
        } catch (IOException e) {
            log.error("解析文件异常", e);
            return null;
        }
    }

    /**
     * 读取文本内容
     *
     * @param html
     * @return
     */
    public Doc text(String html) {
        Document document = Jsoup.parse(html);
        return new Doc(document);
    }

    public Doc text(String html, String baseUri) {
        Document document = Jsoup.parse(html, baseUri);
        return new Doc(document);
    }

    public static class Doc {
        private Document document;
        private JXDocument jXDocument;

        public Doc(Document document) {
            this.document = document;
            this.jXDocument = JXDocument.create(document);
        }

        /**
         * 查找元素：
         * getElementById(String id)
         * getElementsByTag(String tag)
         * getElementsByClass(String className)
         * getElementsByAttribute(String key) (and related methods)
         * Element siblings: siblingElements(), firstElementSibling(), lastElementSibling();nextElementSibling(), previousElementSibling()
         * Graph: parent(), children(), child(int index)
         * <p>
         * 元素数据：
         * attr(String key)获取属性
         * attr(String key, String value)设置属性
         * attributes()获取所有属性
         * id(), className() and classNames()
         * text()获取文本内容text(String value) 设置文本内容
         * html()获取元素内 HTMLhtml(String value)设置元素内的 HTML 内容
         * outerHtml()获取元素外 HTML 内容
         * data()获取数据内容（例如：script 和 style 标签)
         * tag() and tagName()
         * <p>
         * 操作 HTML 和文本：
         * append(String html), prepend(String html)
         * appendText(String text), prependText(String text)
         * appendElement(String tagName), prependElement(String tagName)
         * html(String value)
         */
        public Document getDocument() {
            return document;
        }

        /**
         * CSS 选择器语法：
         * tagname: 通过标签查找元素，比如：a
         * ns|tag: 通过标签在命名空间查找元素，比如：可以用 fb|name 语法来查找 <fb:name></fb:name> 元素
         * #id: 通过 ID 查找元素，比如：#logo
         * .class: 通过 class 名称查找元素，比如：.masthead
         * [attribute]: 利用属性查找元素，比如：[href]
         * [^attr]: 利用属性名前缀来查找元素，比如：可以用[^data-] 来查找带有 HTML5 Dataset 属性的元素
         * [attr=value]: 利用属性值来查找元素，比如：[width=500]
         * [attr^=value], [attr$=value], [attr*=value]: 利用匹配属性值开头、结尾或包含属性值来查找元素，比如：[href*=/path/]
         * [attr~=regex]: 利用属性值匹配正则表达式来查找元素，比如： img[src~=(?i).(png|jpe?g)]
         * *: 这个符号将匹配所有元素
         * <p>
         * 选择器组合：
         * el#id: 元素+ID，比如： div#logo
         * el.class: 元素+class，比如： div.masthead
         * el[attr]: 元素+class，比如： a[href]
         * 任意组合，比如：a[href].highlight
         * ancestor child: 查找某个元素下子元素，比如：可以用.body p 查找在"body"元素下的所有p元素
         * parent > child: 查找某个父元素下的直接子元素，比如：可以用div.content > p 查找 p 元素，也可以用body > * 查找 body 标签下所有直接子元素
         * siblingA + siblingB: 查找在 A 元素之前第一个同级元素 B，比如：div.head + div
         * siblingA ~ siblingX: 查找 A 元素之前的同级 X 元素，比如：h1 \~ p
         * el, el, el:多个选择器组合，查找匹配任一选择器的唯一元素，例如：div.masthead, div.logo
         */
        public List<JXNode> css(String cssQuery) {
            return select(cssQuery);
        }

        public List<JXNode> $(String cssQuery) {
            return css(cssQuery);
        }

        public List<JXNode> select(String cssQuery) {
            Elements elements = document.select(cssQuery);

            List<JXNode> jxNodes = new ArrayList<>();
            for (Element element : elements) {
                jxNodes.add(new JXNode(element));
            }
            return jxNodes;
        }

        public JXDocument getjXDocument() {
            return jXDocument;
        }

        /**
         * xpath 语法
         * <p>
         * 路径表达式：
         * nodename	选取此节点的所有子节点
         * /	从根节点选取
         * //	从匹配选择的当前节点选择文档中的节点，而不考虑它们的位置
         * .	选取当前节点
         * ..	选取当前节点的父节点
         *
         * @	选取属性 函数：
         * int position() 返回当前节点在其所在上下文中的位置
         * int last() 返回所在上下文的最后那个节点位置
         * int first() 返回所在上下文的的第一个节点位置
         * string concat(string, string, string*) 连接若干字符串
         * boolean contains(string, string) 判断第一个字符串是否包含第二个
         * int count(node-set) 计算给定的节点集合中节点个数
         * boolean starts-with(string, string) 判断第一个字符串是否以第二个开头
         * int string-length(string?) 如果给定了字符串则返回字符串长度，如果没有，那么则将当前节点转为字符串并返回长度
         * string substring(string, number, number?) 第一个参数指定字符串，第二个指定起始位置（xpath索引都是从1开始），第三指定要截取的长度，这里要注意在xpath的语法里这，不是结束的位置。
         * string substring-ex(string, number, number) 第一个参数指定字符串，第二个指定起始位置(java里的习惯从0开始)，第三个结束的位置（支持负数），这个是JsoupXpath扩展的函数，方便java习惯的开发者使用。
         * string substring-after(string, string) 在第一个字符串中截取第二个字符串之后的部分
         * string substring-before(string, string) 在第一个字符串中截取第二个字符串之前的部分
         * <p>
         * allText()提取节点下全部文本，取代类似 //div/h3//text()这种递归取文本用法
         * html()获取全部节点的内部的html
         * outerHtml()获取全部节点的 包含节点本身在内的全部html
         * num()抽取节点自有文本中全部数字，如果知道节点的自有文本(即非子代节点所包含的文本)中只存在一个数字，如阅读数，评论数，价格等那么直接可以直接提取此数字出来。如果有多个数字将提取第一个匹配的连续数字。
         * text() 提取节点的自有文本
         * node() 提取所有节点
         */
        public List<JXNode> xpath(String xpath) {
            return sel(xpath);
        }

        public List<JXNode> sel(String xpath) {
            return jXDocument.selN(xpath);
        }

        public JXNode selOne(String path) {
            return jXDocument.selNOne(path);
        }

        @Override
        public String toString() {
            return document.toString();
        }
    }
}
