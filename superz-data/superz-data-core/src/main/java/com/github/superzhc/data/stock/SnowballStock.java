package com.github.superzhc.data.stock;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.data.utils.XueQiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/2 17:34
 */
public class SnowballStock {
    private static final Logger log = LoggerFactory.getLogger(SnowballStock.class);

    private static final String BASE_URL = "https://stock.xueqiu.com";

    /**
     * 现金流量表
     *
     * @param symbol
     * @param quarter Q4
     * @param count
     *
     * @return
     */
    public static Map<String, Object> cashFlow(String symbol, String quarter, Integer count) {
        String path = "/v5/stock/finance/cn/cash_flow.json";

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("type", quarter);
        params.put("count", count);

        String result = execute(path, params);
        System.out.println(result);

        return null;
    }

    private static String execute(String path, Map<?, ?> params) {
        String cookies = XueQiuUtils.cookies();

        String url = String.format("%s%s", BASE_URL, path);

        Map<String, String> HEADERS = new HashMap<>();
        HEADERS.put("Host", "stock.xueqiu.com");
        HEADERS.put("Accept", /*"application/json"*/"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        HEADERS.put("User-Agent", "Xueqiu iPhone 11.8");
        HEADERS.put("Accept-Language", "zh-Hans-CN;q=1, ja-JP;q=0.9");
        HEADERS.put("Accept-Encoding", "br, gzip, deflate");
        HEADERS.put("Connection", "keep-alive");
        HEADERS.put("Cookie", cookies);

        String result = HttpRequest.get(url, params).headers(HEADERS).body("GB2312");
        return result;
    }

    public static void main(String[] args) {
        cashFlow("SH000001", "Q3", 100);
    }
}
