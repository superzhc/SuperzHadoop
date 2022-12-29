package com.github.superzhc.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author superz
 * @create 2022/12/29 21:59
 */
public class HowBuy {
    private static final Logger LOG = LoggerFactory.getLogger(HowBuy.class);

    public static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    public static List<Map<String, Object>> funds() {
        String url = "https://www.howbuy.com/fund/fundranking/ajax.htm";

        Map<String, Object> form = new HashMap<>();
        form.put("orderField", "");
        form.put("orderType", "");
        form.put("glrm", "");
        form.put("keyword", "");
        form.put("bd", "");
        form.put("ed", "");
        form.put("radio", "");
        form.put("page", 1);
        form.put("cat", "index.htm");

        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            form.put("page", i);
            String result = HttpRequest.post(url).userAgent(UA_CHROME).form(form).body();
            JsonNode json = JsonUtils.loads(result, "list");
            for (JsonNode node : json) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("code", JsonUtils.string(node, "jjdm"));
                item.put("name", JsonUtils.string(node, "jjjc"));
                item.put("company_code", JsonUtils.string(node, "jgdm"));
                item.put("type", JsonUtils.string(node, "jjfl"));
                item.put("nature", JsonUtils.string(node, "jjxz"));

                data.add(item);
            }
        }

        return data;
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(funds()));
    }
}
