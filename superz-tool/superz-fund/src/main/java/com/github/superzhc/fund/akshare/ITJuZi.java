package com.github.superzhc.fund.akshare;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.json.JsonReadOptions;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/3/31 11:17
 */
public class ITJuZi {
    private static final Logger log = LoggerFactory.getLogger(ITJuZi.class);

    /**
     * 独角兽
     *
     * @return
     */
    public static Table unicorn() {
        try {
            Table table = Table.read().csv(new URL("https://jfds-1252952517.cos.ap-chengdu.myqcloud.com/akshare/data/data_juzi/nicorn_company.csv"));
            System.out.println(table.print());
            System.out.println("\n\n--------------------------\n\n");

            for (int i = 1; i <= 2; i++) {
                String url = "https://www.itjuzi.com/api/maxima";

                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

                Map<String, String> params = new HashMap<>();
                params.put("page", String.valueOf(i));

                String json = HttpRequest.get(url, params).headers(headers).body();
                System.out.println(json);
                System.out.println("\n\n--------------------------\n\n");

                JsonReadOptions options = JsonReadOptions.builderFromString(json).path("/data/data").build();
                Table t = Table.read().usingOptions(options);
                System.out.println(t.print());
                System.out.println("\n\n--------------------------\n\n");
            }

            return table;
        } catch (IOException e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Table table = unicorn();
        System.out.println(table.print());
    }
}
