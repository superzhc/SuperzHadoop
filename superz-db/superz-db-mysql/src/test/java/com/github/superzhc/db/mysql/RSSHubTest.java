package com.github.superzhc.db.mysql;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.jdbc.JdbcHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author superz
 * @create 2023/5/10 14:04
 **/
public class RSSHubTest {
    String url = "jdbc:mysql://127.0.0.1:3306/ry-vue?useSSL=false&characterEncoding=utf-8";
    String username = "root";
    String password = "123456";

    private String urlTemplate = "https://rsshub.app/%s.json";

    JdbcHelper jdbc;

    @Before
    public void setUp() {
        jdbc = new JdbcHelper(url, username, password);
    }

    @After
    public void tearDown() {
        jdbc.close();
    }

    @Test
    public void test() {
        String url = String.format(urlTemplate, "jiemian");
        String result = HttpRequest.get(url)
                .useProxy("127.0.0.1", 10809)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                .body();

        JsonNode json = JsonUtils.loads(result);

        String type = JsonUtils.string(json, "title");

        List<List<Object>> params = new ArrayList<>();

        String sql = "insert into t_rsshub (guid, title, author, content, publish_date, link, type) values (?,?,?,?,?,?,?)";

        for (JsonNode item : json.get("items")) {
            Object[] param = new Object[7];
            param[0] = JsonUtils.string(item, "id");
            param[1] = JsonUtils.string(item, "title");
            param[2] = JsonUtils.asString(item.get("authors"));
            param[3] = JsonUtils.string(item, "content_html");
            param[4] = JsonUtils.date(item, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "date_published");
            param[5] = JsonUtils.string(item, "url");
            param[6] = type;

             params.add(Arrays.asList(param));
//            jdbc.dmlExecute(sql, param);
        }

        jdbc.batchUpdate(sql,params);
    }
}
