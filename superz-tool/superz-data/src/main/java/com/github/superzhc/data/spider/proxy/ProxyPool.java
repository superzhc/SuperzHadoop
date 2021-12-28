package com.github.superzhc.data.spider.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2021/12/27 18:13
 */
public class ProxyPool {

    private static final Logger log = LoggerFactory.getLogger(ProxyPool.class);

    private static final String REGEX_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,5}";

    private static volatile ProxyPool instance;

    private List<String> proxies;

    private ProxyPool() {
        this.proxies = new ArrayList<>();
    }

    public void init() {
        log.info("代理文件读取开始");
        try (InputStream inputStream = new FileInputStream(new File(this.getClass().getResource("/proxy.txt").getPath()))) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    Pattern p = Pattern.compile(REGEX_IP);

                    String str;
                    while ((str = reader.readLine()) != null) {
                        // 验证ip格式是否正确
                        if (!p.matcher(str).matches()) {
                            continue;
                        }

                        // 验证代理ip的有效性么，有点耗时~~~

                        proxies.add(str);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("代理文件读取完成，读取代理数量：" + proxies.size());
    }

    public static ProxyPool getInstance() {
        if (null == instance) {
            synchronized (ProxyPool.class) {
                if (null == instance) {
                    instance = new ProxyPool();
                    instance.init();
                }
            }
        }
        return instance;
    }

    public SimpleProxyProvider getSimpleProxyProvider() {
        Proxy[] cProxies = new Proxy[proxies.size()];
        for (int i = 0, len = cProxies.length; i < len; i++) {
            String[] ss = proxies.get(i).split(":");
            if (ss.length == 2) {
                cProxies[i] = new Proxy(ss[0], Integer.valueOf(ss[1]));
            }
        }
        return SimpleProxyProvider.from(cProxies);
    }

    public HttpClientDownloader getHttpClientDownloader() {
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(getSimpleProxyProvider());
        return downloader;
    }
}
