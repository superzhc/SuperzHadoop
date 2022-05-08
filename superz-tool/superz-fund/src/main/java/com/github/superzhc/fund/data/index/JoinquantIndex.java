package com.github.superzhc.fund.data.index;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlReadOptions;

/**
 * @author superz
 * @create 2022/5/6 19:34
 **/
public class JoinquantIndex {
    private static final Logger log = LoggerFactory.getLogger(JoinquantIndex.class);

    public static Table indices() {
        String url = "https://www.joinquant.com/data/dict/indexData";
        String result = HttpRequest.get(url).body();
        HtmlReadOptions options = HtmlReadOptions.builderFromString(result)
                .tableIndex(0)
                .build();

        try {
            Table table = Table.read().usingOptions(options);
            return table;
        } catch (Exception e) {
            log.error("解析异常", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Table table = indices();
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().print());
    }
}
