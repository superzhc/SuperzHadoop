package com.github.superzhc.fund.akshare;

import com.github.superzhc.common.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/21 15:07
 **/
public class XueQiu {
    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36";

    /**
     * 建议多次复用获取的 cookies，不要每次都去调用这个方法
     *
     * @return
     */
    public static String cookies() {
        Map<String, String> map = HttpRequest.get("https://xueqiu.com").userAgent(UA).cookies();
        String xqAToken = map.get("xq_a_token");
        return String.format("xq_a_token=%s", xqAToken);
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

        String result = HttpRequest.get(url, params).userAgent(UA).cookies(cookies).body();
        System.out.println(result);
    }
}
