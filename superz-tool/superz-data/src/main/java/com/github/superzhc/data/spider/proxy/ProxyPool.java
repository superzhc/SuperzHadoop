package com.github.superzhc.data.spider.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2021/12/27 18:13
 */
public class ProxyPool {

    private static final Logger log= LoggerFactory.getLogger(ProxyPool.class);

    private static volatile ProxyPool instance;

    private List<String> proxies;

    private ProxyPool() {
        this.proxies = new ArrayList<>();
    }

    public void init() {
        log.info("读取代理文件");
        try (InputStream inputStream = new FileInputStream(new File(this.getClass().getResource("/proxy.txt").getPath()))) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String str;
                    while ((str = reader.readLine()) != null) {
                        proxies.add(str);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("代理文件读取完成");
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
