package com.github.superzhc.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/12/30 0:24
 */
public class SinaFund {
    private static final Logger LOG = LoggerFactory.getLogger(SinaFund.class);

    /**
     * SQL见表语句：create table sina_funds(id bigint auto_increment primary key,code varchar(8) not null,name varchar(255) not null,company_name varchar(255),subject_name varchar(255),create_date varchar(50),scale double,cxpj int,htpj int,jajxpj int,zspj int,yhpj3 int,yhpj5 int)
     *
     * @return
     */
    public static List<Map<String, Object>> funds() {
        String url = "http://stock.finance.sina.com.cn/fundfilter/api/openapi.php/MoneyFinanceFundFilterService.getFundFilterAll";

        Map<String, Object> params = new HashMap<>();
        params.put("callback", "makeFilterData");
        params.put("dpc", "1");
        params.put("page", 1);
        params.put("num", 1000);

        List<Map<String, Object>> data = new ArrayList<>();

        int size = 1000;
        int pages = 19594 / 1000 + 1;
        for (int i = 1; i <= pages; i++) {
            params.put("page", i);
            params.put("num", (19594 - (i - 1) * size) > size ? size : (19594 - (i - 1) * size));
            String result = HttpRequest.get(url, params).body();

            Pattern pattern = Pattern.compile("makeFilterData\\(([\\s\\S]+)\\)");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                result = matcher.group(1);
            }
            JsonNode json = JsonUtils.loads(result, "result", "data", "data");
            for (JsonNode node : json) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("code", JsonUtils.string(node, "symbol"));
                map.put("name", JsonUtils.string(node, "name"));
                map.put("company_name", JsonUtils.string(node, "CompanyName"));
                map.put("subject_name", JsonUtils.string(node, "SubjectName"));
                map.put("create_date", JsonUtils.string(node, "clrq"));
                map.put("scale", JsonUtils.aDouble(node, "jjgm"));
                map.put("cxpj", JsonUtils.integer(node, "cxpj"));
                map.put("htpj", JsonUtils.integer(node, "htpj"));
                map.put("jajxpj", JsonUtils.integer(node, "jajxpj"));
                map.put("zspj", JsonUtils.integer(node, "zspj"));
                map.put("yhpj3", JsonUtils.integer(node, "yhpj3"));
                map.put("yhpj5", JsonUtils.integer(node, "yhpj5"));

                data.add(map);
            }

        }

        return data;
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(funds(), 100));
    }
}
