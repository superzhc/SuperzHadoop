package com.github.superzhc.data.spider.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 参考代码：
 * https://github.com/jhao104/proxy_pool/blob/master/fetcher/proxyFetcher.py
 * <p>
 * 注意事项：免费代理的质量不会很高
 *
 * @author superz
 * @create 2021/12/27 13:35
 */
public class ProxyFetcher implements PageProcessor {
    private Site site = Site.me()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
            .setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        switch (url) {
            case FreeProxy_MiMvp.URL1:
            case FreeProxy_MiMvp.URL2:
                FreeProxy_MiMvp.process(page);
                break;
            case FreeProxy_66ip.URL:
                FreeProxy_66ip.process(page);
                break;
            case FreeProxy_KXDaiLi.URL1:
            case FreeProxy_KXDaiLi.URL2:
                FreeProxy_KXDaiLi.process(page);
                break;
            case FreeProxy_DieNiao.URL:
                FreeProxy_DieNiao.process(page);
                break;
            case FreeProxy_Proxy11.URL:
                FreeProxy_Proxy11.process(page);
                break;
            case FreeProxy_KuaiDaiLi.URL1:
            case FreeProxy_KuaiDaiLi.URL2:
                FreeProxy_KuaiDaiLi.process(page);
                break;
            case FreeProxy_Ip3366.URL1:
            case FreeProxy_Ip3366.URL2:
                FreeProxy_Ip3366.process(page);
                break;
            case FreeProxy_IHuan.URL:
                FreeProxy_IHuan.process(page);
                break;
            case FreeProxy_89Ip.URL:
                FreeProxy_89Ip.process(page);
                break;
            case FreeProxy_JiangXianLi.URL1:
                FreeProxy_JiangXianLi.process(page);
                break;
            default:
                throw new RuntimeException("尚不支持地址" + url);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static class FreeProxy_MiMvp {
        public static final String URL1 = "https://proxy.mimvp.com/freeopen?proxy=in_hp";
        public static final String URL2 = "https://proxy.mimvp.com/freeopen?proxy=out_hp";

        public static Map<String, Integer> portImgMap;

        static {
            portImgMap = new HashMap<>();
            portImgMap.put("DExMA", 110);
            portImgMap.put("DMxMjg", 3128);
            portImgMap.put("DQyMDU1", 42055);
            portImgMap.put("DUzMjgx", 53281);
            portImgMap.put("DU1NDQz", 55443);
            portImgMap.put("Dgw", 80);
            portImgMap.put("DgwOA", 808);
            portImgMap.put("DgwMDA", 8000);
            portImgMap.put("DgwMDE", 8001);
            portImgMap.put("DgwODA", 8080);
            portImgMap.put("DgwODE", 8081);
            portImgMap.put("Dg4ODg", 8888);
            portImgMap.put("Dk5OTk", 9999);
        }

        public static void process(Page page) {
            Html html = page.getHtml();
            List<Selectable> nodes = html.xpath("//table[@class='mimvp-tbl free-proxylist-tbl']/tbody/tr").nodes();
            for (Selectable node : nodes) {
                String ip = node.xpath("//td[2]/text()").get().trim();
                String encryptPort = node.xpath("//td[3]/img/@src").get();
                String encryptPort2 = (encryptPort.split("port=")[1]).substring(14).replace("O0O", "");
                if (StringUtils.isNotBlank(ip) && portImgMap.containsKey(encryptPort2)) {
                    page.putField(UUID.randomUUID().toString(), String.format(String.format("%s:%d", ip, portImgMap.get(encryptPort2))));
                }
            }
        }
    }

    private static class FreeProxy_66ip {
        public static final String URL = "http://www.66ip.cn/mo.php";

        public static void process(Page page) {
            Html html = page.getHtml();
            List<String> proxies = html.regex("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,5})").all();
            for (String proxy : proxies) {
                page.putField(UUID.randomUUID().toString(), proxy);
            }
        }
    }

    private static class FreeProxy_KXDaiLi {
        public static final String URL1 = "http://www.kxdaili.com/dailiip.html";
        public static final String URL2 = "http://www.kxdaili.com/dailiip/2/1.html";

        public static void process(Page page) {
            Html html = page.getHtml();
            List<Selectable> nodes = html.xpath("//table[@class='active']//tr").nodes();
            for (int i = 1, len = nodes.size(); i < len; i++) {
                Selectable node = nodes.get(i);
                String ip = node.xpath("//td[1]/text()").get().trim();
                String port = node.xpath("//td[2]/text()").get().trim();
                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                    page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ip, port));
                }
            }
        }
    }

    private static class FreeProxy_DieNiao {
        public static final String URL = "https://www.dieniao.com/FreeProxy.html";

        public static void process(Page page) {
            Html html = page.getHtml();
            List<Selectable> nodes = html.xpath("//div[@class='free-main col-lg-12 col-md-12 col-sm-12 col-xs-12']/ul/li").nodes();
            for (int i = 1, len = nodes.size(); i < len; i++) {
                Selectable node = nodes.get(i);
                String ip = node.xpath("//span[1]/text()").get().trim();
                String port = node.xpath("//span[2]/text()").get().trim();
                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                    page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ip, port));
                }
            }
        }
    }

    private static class FreeProxy_KuaiDaiLi {
        private static final String URL1 = "https://www.kuaidaili.com/free/inha/1/";
        private static final String URL2 = "https://www.kuaidaili.com/free/intr/1/";

        public static void process(Page page) {
            Html html = page.getHtml();

            List<Selectable> nodes = html.xpath("//table//tr").nodes();
            for (int i = 1, len = nodes.size(); i < len; i++) {
                Selectable node = nodes.get(i);
                String ip = node.xpath("//td[1]/text()").get();
                String port = node.xpath("//td[2]/text()").get();
                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                    page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ip, port));
                }
            }
        }
    }

    private static class FreeProxy_Proxy11 {
        // https://proxy11.com/api/demoweb/proxy.json?country=hk&speed=2000
        public static final String URL = "https://proxy11.com/api/demoweb/proxy.json?speed=2000";

        private static final ObjectMapper mapper = new ObjectMapper();

        public static void process(Page page) {
            Json json = page.getJson();
            try {
                JsonNode node = mapper.readTree(json.get());
                JsonNode proxies = node.get("data");
                if (proxies.isArray()) {
                    for (JsonNode proxy : proxies) {
                        page.putField(UUID.randomUUID().toString(), String.format("%s:%s", proxy.get("ip").asText(), proxy.get("port").asText()));
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private static class FreeProxy_Ip3366 {
        public static final String URL1 = "http://www.ip3366.net/free/?stype=1";
        public static final String URL2 = "http://www.ip3366.net/free/?stype=2";

        public static void process(Page page) {
            Html html = page.getHtml();
            List<String> ips = html.regex("<td>(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})</td>[\\s\\S]*?<td>(\\d+)</td>", 1).all();
            List<String> ports = html.regex("<td>(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})</td>[\\s\\S]*?<td>(\\d+)</td>", 2).all();

            for (int i = 0, len = Math.min(ips.size(), ports.size()); i < len; i++) {
                page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ips.get(i), ports.get(i)));
            }
        }
    }

    /**
     * 小幻代理
     */
    private static class FreeProxy_IHuan {
        private static final String URL = "https://ip.ihuan.me/address/5Lit5Zu9.html";

        public static void process(Page page) {
            Html html = page.getHtml();

            List<Selectable> nodes = html.xpath("//table[@class=\"table table-hover table-bordered\"]/tbody/tr").nodes();
            for (Selectable node : nodes) {
                String ip = node.xpath("//td[1]/a/text()").get();
                String port = node.xpath("//td[2]/text()").get();
                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                    page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ip, port));
                }
            }
        }
    }

    private static class FreeProxy_JiangXianLi {
        // page分页，理论上可查询多条的，但是爬虫不可用，后期需要时候再优化
        private static final String URL1 = "http://ip.jiangxianli.com/?country=中国&page=1";
        //private static final String URL2 = "http://ip.jiangxianli.com/?country=中国&page=2";
        //private static final String URL3 = "http://ip.jiangxianli.com/?country=中国&page=3";

        public static void process(Page page) {
            Html html = page.getHtml();

            List<Selectable> nodes = html.xpath("//table//tr").nodes();
            for (int i = 1, len = nodes.size(); i < len; i++) {
                Selectable node = nodes.get(i);
                String ip = node.xpath("//td[1]/text()").get();
                String port = node.xpath("//td[2]/text()").get();
                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                    page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ip, port));
                }
            }
        }
    }

    private static class FreeProxy_89Ip {
        private static final String URL = "https://www.89ip.cn/index_1.html";

        public static void process(Page page) {
            Html html = page.getHtml();

            List<String> ips = html.regex("<td.*?>[\\s\\S]*?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})[\\s\\S]*?</td>[\\s\\S]*?<td.*?>[\\s\\S]*?(\\d+)[\\s\\S]*?</td>", 1).all();
            List<String> ports = html.regex("<td.*?>[\\s\\S]*?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})[\\s\\S]*?</td>[\\s\\S]*?<td.*?>[\\s\\S]*?(\\d+)[\\s\\S]*?</td>", 2).all();
            for (int i = 0, len = Math.min(ips.size(), ports.size()); i < len; i++) {
                String ip = ips.get(i);
                String port = ports.get(i);
                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
                    page.putField(UUID.randomUUID().toString(), String.format("%s:%s", ip, port));
                }
            }
        }
    }

    public static void main(String[] args) {
        final String path = "D:\\code\\SuperzHadoop\\superz-tool\\superz-data\\src\\main\\resources\\proxy.txt";

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        Spider.create(new ProxyFetcher())
                // .addUrl(FreeProxy_MiMvp.URL1, FreeProxy_MiMvp.URL2)
                // .addUrl(FreeProxy_66ip.URL)
                // .addUrl(FreeProxy_KXDaiLi.URL1, FreeProxy_KXDaiLi.URL2)
                // .addUrl(FreeProxy_DieNiao.URL)
                // .addUrl(FreeProxy_Proxy11.URL)
                // .addUrl(FreeProxy_KuaiDaiLi.URL1, FreeProxy_KuaiDaiLi.URL2)
                // .addUrl(FreeProxy_Ip3366.URL1, FreeProxy_Ip3366.URL2)
                .addUrl(FreeProxy_IHuan.URL)
                // .addUrl(FreeProxy_JiangXianLi.URL1)
                // .addUrl(FreeProxy_89Ip.URL)
                .addPipeline(new Pipeline() {
                    private Logger logger = LoggerFactory.getLogger(ProxyFetcher.class);

                    @Override
                    public void process(ResultItems resultItems, Task task) {
                        File file = new File(path);
                        try {
                            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                                // 逐行输出即可
                                printWriter.append((String) entry.getValue()).append("\n");
                            }
                            printWriter.close();
                        } catch (IOException e) {
                            logger.warn("write file error", e);
                        }
                    }
                })
                .run();
    }
}
