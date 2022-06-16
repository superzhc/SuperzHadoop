package com.github.superzhc.common.html.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 参考：https://github.com/hellysmile/fake-useragent
 *
 * @author superz
 * @create 2022/1/10 15:47
 */
public class UserAgentSpider {
    private static final Logger log = LoggerFactory.getLogger(UserAgentSpider.class);

    public static enum Browser {
        /**
         * 浏览器种类
         */
        Chrome, Edge("Internet Explorer"), IE("Internet Explorer"), Firefox, Mozilla, Safari, Opera, Netscape;

        private final String value;

        private Browser() {
            value = name();
        }

        private Browser(String name) {
            value = name;
        }

        public String value() {
            return this.value;
        }
    }

    public static final String BROWSER_BASE_PAGE = "http://useragentstring.com/pages/useragentstring.php?name=%s";

    private static Map<String, List<String>> cache = new ConcurrentHashMap<>();

    public List<String> get() {
        List<String> userAgents = new ArrayList<>();
        for (Browser browser : Browser.values()) {
            userAgents.addAll(get(browser));
        }
        return userAgents;
    }

    public List<String> get(Browser browser) {
        if (!cache.containsKey(browser.name())) {
            synchronized (this) {
                if (!cache.containsKey(browser.name())) {
                    try {
                        List<String> userAgents = new ArrayList<>();
                        Document document = Jsoup.connect(String.format(BROWSER_BASE_PAGE, browser.value()))
                                //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                                .get();
                        Elements elements = document.getElementById("liste").select("li").select("a[href]");
                        for (Element element : elements) {
                            userAgents.add(element.text());
                        }
                        cache.put(browser.name(), userAgents);
                    } catch (IOException e) {
                        log.error("获取浏览器[" + browser.value() + "]异常", e);
                        cache.put(browser.name(), new ArrayList<>());
                    }
                }
            }
        }
        return cache.get(browser.name());
    }

    public static String IE() {
        return random(Browser.IE);
    }

    public static String Google() {
        return Chrome();
    }

    public static String Chrome() {
        return random(Browser.Chrome);
    }


    public static String Edge() {
        return random(Browser.Edge);
    }

    public static String Firefox() {
        return random(Browser.Firefox);
    }

    public static String Mozilla() {
        return random(Browser.Mozilla);
    }

    public static String Safari() {
        return random(Browser.Safari);
    }

    public static String Opera() {
        return random(Browser.Opera);
    }

    public static String Netscape() {
        return random(Browser.Netscape);
    }

    public static String random() {
        Browser browser = Browser.values()[(int) (Math.random() * Browser.values().length)];
        return random(browser);
    }

    public static String random(Browser browser) {
        if (!cache.containsKey(browser.name())) {
            new UserAgentSpider().get(browser);
        }

        List<String> userAgents = cache.get(browser.name());
        return userAgents.get((int) (Math.random() * userAgents.size()));
    }

    public static void main(String[] args) throws Exception {
//        List<String> userAgents = new UserAgent().get(Browser.Edge);
//        System.out.println(userAgents.stream().collect(Collectors.joining("\n")));
//        while (true) {
            System.out.println(UserAgentSpider.Chrome());
//            Thread.sleep(1000);
//        }
    }
}
