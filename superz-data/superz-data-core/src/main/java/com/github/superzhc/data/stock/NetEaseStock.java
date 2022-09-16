package com.github.superzhc.data.stock;

import com.github.superzhc.common.http.HttpRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2022/9/15 0:44
 */
public class NetEaseStock {

    public static List<Map<String, Object>> historys(String... symbols) {
        List<Map<String, Object>> lst = new ArrayList<>();
        for (String symbol : symbols) {
            lst.addAll(history(symbol));
        }
        return lst;
    }

    public static List<Map<String, Object>> history(String symbol) {
        String url = "http://quotes.money.163.com/service/chddata.html";

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Cache-Control", "no-cache");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "quotes.money.163.com");
        headers.put("Pragma", "no-cache");
        headers.put("Referer", "http://quotes.money.163.com/trade/lsjysj_300254.html");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36");

        Map<String, Object> params = new HashMap<>();
        params.put("code", String.format("%s%s", symbol.startsWith("sh") ? "0" : "1"/*sh 0,sz 1*/, symbol.substring(2)));
        params.put("start", "19700101");
        params.put("end", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        params.put("fields", "TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP");

        String result = HttpRequest.get(url, params).headers(headers).body("gbk");
        String[] data = result.split("\r\n");

        List<Map<String, Object>> dataRows = new ArrayList<>(data.length - 1);

        String[] keys = data[0].split(",");
        for (int i = 1, len = data.length; i < len; i++) {
            String[] item = data[i].split(",", -1);

            Map<String, Object> dataRow = new LinkedHashMap<>();
            for (int j = 0, l = Math.min(keys.length, item.length); j < l; j++) {
                dataRow.put(keys[j], item[j]);
            }
            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        history(/*"sh601318"*/"sh000300");
    }
}
