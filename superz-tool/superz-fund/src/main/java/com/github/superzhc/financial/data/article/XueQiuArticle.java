package com.github.superzhc.financial.data.article;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.XueQiuUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.tablesaw.utils.TableReaderUtils;
import com.github.superzhc.common.tablesaw.utils.TableWriterUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * @author superz
 * @create 2022/8/5 9:12
 **/
public class XueQiuArticle {

    public static void userArticle(String userId) {
        String url = "https://xueqiu.com/v4/statuses/user_timeline.json";

        Integer pages = null;

        List<String[]> dataRows = new ArrayList<>();

        /**
         * 超过5页，需要login ???
         */

        for (int i = 5; (pages == null || i < pages); i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("page", (i + 1));
            params.put("user_id", userId);

            String result = HttpRequest.get(url, params).cookies(XueQiuUtils.cookies()).body();
            JsonNode json = JsonUtils.json(result);

            if (null == pages) {
                pages = JsonUtils.integer(json, "maxPage");
            }

            JsonNode articles = json.get("statuses");
            for (int j = 0, len = articles.size(); j < len; j++) {
                JsonNode article = articles.get(j);
                String[] dataRow = new String[]{JsonUtils.asString(article)};
                dataRows.add(dataRow);
            }
        }

        Table table = TableUtils.build(Arrays.asList("col1"), dataRows);
        try (JdbcHelper jdbc = new JdbcHelper("jdbc:mysql://rm-bp1cg3i12uzu01y4jjo.mysql.rds.aliyuncs.com:3306?useSSL=false", "superz", "Zhengch@o")) {
            TableWriterUtils.db(jdbc, table, "xueqiu_article_liuyijushi");
        }
    }

    public static void main(String[] args) {
        // 六亿居士
        userArticle("9391624441");
    }
}
