package com.github.superzhc.common.html;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参考：https://github.com/hellysmile/fake-useragent
 * <p>
 * 2022年9月20日 访问地址发生迁移，匹配正则也发生变化
 *
 * @author superz
 * @create 2022/1/10 15:47
 */
public class UserAgent {
    private static final Logger log = LoggerFactory.getLogger(UserAgent.class);

    public static enum BrowserType {
        /**
         * 浏览器种类
         */
        Chrome, Edge("Internet Explorer"), IE("Internet Explorer"), Firefox, Mozilla, Safari, Opera, Netscape;

        private final String value;

        private BrowserType() {
            value = name();
        }

        private BrowserType(String name) {
            value = name;
        }

        public String value() {
            return this.value;
        }
    }

    public static final String BROWSER_BASE_PAGE = "https://useragentstring.com/pages/useragentstring.php?name=%s";

    private static Map<String, List<String>> cache = new ConcurrentHashMap<>();

    public List<String> get() {
        List<String> userAgents = new ArrayList<>();
        for (BrowserType browserType : BrowserType.values()) {
            userAgents.addAll(get(browserType));
        }
        return userAgents;
    }

    public List<String> get(BrowserType browserType) {
        if (!cache.containsKey(browserType.name())) {
            synchronized (this) {
                if (!cache.containsKey(browserType.name())) {
                    try {
                        List<String> userAgents = new ArrayList<>();
                        // 因 core 包不依赖任何第三方库，此处使用正则表达式进行提取
                        // Document document = Jsoup.connect(String.format(BROWSER_BASE_PAGE, browserType.value()))
                        //         //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                        //         .get();
                        // Elements elements = document.getElementById("liste").select("li").select("a[href]");
                        // for (Element element : elements) {
                        //     userAgents.add(element.text());
                        // }
                        String result = HttpRequest.get(String.format(BROWSER_BASE_PAGE, browserType.value())).body();
                        // String regex = "<li><a href=(['\"])\\/index\\.php\\?id=(\\d*)(['\"])>([\\s\\S]*?)<\\/a><\\/li>";
                        String regex = "<a href=(['\"])([\\s\\S]*?)(['\"])>([\\s\\S]*?)<\\/a>";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(result);
                        while (matcher.find()) {
                            // System.out.println(matcher.group(4));
                            userAgents.add(matcher.group(4));
                        }
                        cache.put(browserType.name(), userAgents);
                    } catch (Exception e) {
                        log.error("获取浏览器[" + browserType.value() + "]异常", e);
                        cache.put(browserType.name(), new ArrayList<>());
                    }
                }
            }
        }
        return cache.get(browserType.name());
    }

    public static String IE() {
        return random(BrowserType.IE);
    }

    public static String Google() {
        return Chrome();
    }

    public static String Chrome() {
        return random(BrowserType.Chrome);
    }

    public static String Edge() {
        return random(BrowserType.Edge);
    }

    public static String Firefox() {
        return random(BrowserType.Firefox);
    }

    public static String Mozilla() {
        return random(BrowserType.Mozilla);
    }

    public static String Safari() {
        return random(BrowserType.Safari);
    }

    public static String Opera() {
        return random(BrowserType.Opera);
    }

    public static String Netscape() {
        return random(BrowserType.Netscape);
    }

    public static String random() {
        BrowserType browserType = BrowserType.values()[(int) (Math.random() * BrowserType.values().length)];
        return random(browserType);
    }

    public static String random(BrowserType browserType) {
        if (!cache.containsKey(browserType.name())) {
            new UserAgent().get(browserType);
        }

        List<String> userAgents = cache.get(browserType.name());
        return userAgents.get((int) (Math.random() * userAgents.size()));
    }

    public static void main(String[] args) throws Exception {
        List<String> lst = new UserAgent().get();
        for (String str : lst) {
            System.out.println(str);
            System.out.println("-----------------------------------------");
        }
    }
}
