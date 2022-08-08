package com.github.superzhc.financial.utils;

import com.github.superzhc.common.HttpConstant;
import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/21 15:07
 **/
public class XueQiuUtils {
    private static final Logger log = LoggerFactory.getLogger(XueQiuUtils.class);
    private static volatile String COOKIES = null;
    private static volatile LocalDateTime EXPIRED = null;

    /**
     * 建议多次复用获取的 cookies，不要每次都去调用这个方法
     *
     * @return
     */
    public static synchronized String cookies() {
        if (null == COOKIES || EXPIRED.isBefore(LocalDateTime.now())) {
            Map<String, String> map = HttpRequest.get("https://xueqiu.com").userAgent(HttpConstant.UA).cookies();
            String xqAToken = map.get("xq_a_token");
            COOKIES = String.format("xq_a_token=%s", xqAToken);
            EXPIRED = LocalDateTime.now().plusMinutes(10);
        } else {
            EXPIRED = LocalDateTime.now().plusMinutes(10);
        }
        log.debug("XueQiu Cookies[{},{}]", EXPIRED, COOKIES);
        return COOKIES;
    }

    public static void main(String[] args) {
        String cookies = cookies();

        String url = "https://xueqiu.com/stock/quote_order.json";

        Map<String, Object> params = new HashMap<>();
        params.put("stockType", "sha");
        params.put("order", "desc");
        params.put("orderBy", "amount");
        params.put("size", 200);
        params.put("page", 1);
        params.put("column", "symbol,name");

        String result = HttpRequest.get(url, params).userAgent(HttpConstant.UA).cookies(cookies).body();
        System.out.println(result);
    }
}
