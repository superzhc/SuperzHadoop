package com.github.superzhc.hadoop.es;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.shopping.GuangDiu;
import com.github.superzhc.hadoop.es.document.ESDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/12 11:50
 **/
public class ESDocumentTest extends ESClientTest {
    ESDocument docClient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        docClient = new ESDocument(client);
    }

    @Test
    public void testUpsert() {
        List<Map<String, Object>> data = GuangDiu.all();

        for (Map<String, Object> item : data) {
            String id = String.valueOf(item.get("id"));
            docClient.upsert("my_test", id, JsonUtils.asString(item));
        }
    }
}
