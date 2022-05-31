package com.github.superzhc.data.xgit;

import com.github.superzhc.common.jdbc.JdbcHelper;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/8 18:08
 **/
public class TxtWriter {
    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        try (JdbcHelper jdbc = new JdbcHelper(jdbcUrl, username, password)) {
            String fileName = "xgit_spider_" + System.currentTimeMillis() + ".txt";
            String path = "E:\\data\\" + fileName;
            try (FileWriter writer = new FileWriter(path)) {
                List<Map<String, Object>> rows = jdbc.query("select title,brief,content from xgit_spider");
                int cursor = 0;
                for (Map<String, Object> row : rows) {
                    String title = (String) row.get("title");
                    String brief = (String) row.get("brief");
                    String c = (String) row.get("content");
                    if (title.contains("<") && title.contains(">")) {
                        continue;
                    }
                    if (brief.contains("<") && brief.contains(">")) {
                        continue;
                    }

                    if (c.contains("###")) {
                        continue;
                    }

                    String content = String.format("%s###%s###%s\n", title, brief, row.get("content"));
                    writer.append(content);
                    cursor++;

                    if (cursor > 1000) {
                        break;
                    }
                }
            }
        }
    }
}
