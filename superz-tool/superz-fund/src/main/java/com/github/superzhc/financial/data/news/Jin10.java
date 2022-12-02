package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/5/8 15:57
 **/
public class Jin10 {
    public static Table news() {
        return news(LocalDateTime.now());
    }

    public static Table news(LocalDateTime dt) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.news.Jin10.news(dt));

        StringColumn content = table.stringColumn("data").map(str -> {
            if (null == str || str.trim().length() == 0) {
                return "";
            }

            JsonNode data = JsonUtils.json(str);
            if (data.has("content")) {
                String s = data.get("content").asText();
                if (data.has("pic") &&
                        (data.get("pic") != null && data.get("pic").asText().trim().length() > 0)) {
                    s += "<" + JsonUtils.string(data, "pic") + ">";
                }
                return s;
            } else {
                return str;
            }
        }).setName("content");
        table.replaceColumn("data", content);
        table.setName("news_jin10");

        return table;
    }

    public static void main(String[] args) {
        Table table = news();

        System.out.println(table.print(200));
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());

        TableUtils.write2Html(table);
    }
}
