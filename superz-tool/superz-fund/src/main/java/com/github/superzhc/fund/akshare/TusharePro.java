package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import org.apache.commons.lang3.StringUtils;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/8 0:36
 */
public class TusharePro {
    private static final String URL = "http://api.tushare.pro";

    private String token;

    public TusharePro(String token) {
        this.token = token;
    }

    public Table indexBasic() {
        return indexBasic(null, null, null, null, null);
    }

    /**
     * 获取指数基础信息
     *
     * @param tsCode 指数代码
     * @param name 指数简称
     * @param market 交易所或服务商 MSCI-MSCI指数;CSI-中证指数;SSE-上交所指数;SZSE-深交所指数;CICC-中金指数;SW-申万指数;OTH-其他指数
     * @param publisher 发布商
     * @param category 指数类别
     *
     * @return
     */
    public Table indexBasic(String tsCode, String name, String market, String publisher, String category) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(tsCode)) {
            params.put("ts_code", tsCode);
        }
        if (StringUtils.isNotBlank(name)) {
            params.put("name", name);
        }
        if (StringUtils.isNotBlank(market)) {
            params.put("market", market);
        }
        if (StringUtils.isNotBlank(publisher)) {
            params.put("publisher", publisher);
        }
        if (StringUtils.isNotBlank(category)) {
            params.put("category", category);
        }

        String[] fields = new String[]{
                "ts_code",
                "name",
                "fullname",
                "market",
                "publisher",
                "index_type",
                "category",
                "base_date",
                "base_point",
                "list_date",
                "weight_rule",
                "desc",
                "exp_date"
        };

        return execute("index_basic", params, String.join(",", fields));
    }

    private Table execute(String apiName, Map<String, String> params, String fields) {
        Map<String, Object> json = new HashMap<>();
        json.put("api_name", apiName);
        json.put("token", token);
        if (null == params) {
            params = new HashMap<>();
        }
        json.put("params", params);
        if (StringUtils.isNotBlank(fields)) {
            json.put("fields", fields);
        }

        String result = HttpRequest.post(URL).json(JsonUtils.string(json)).body();
        JsonNode data = JsonUtils.json(result, "data");

        List<String> columnNames = new ArrayList<>();
        for (JsonNode field : data.get("fields")) {
            columnNames.add(field.asText());
        }

        List<String[]> dataRows = JsonUtils.extractArrayData(data.get("items"));

        return TableUtils.build(columnNames, dataRows);
    }

    public static void main(String[] args) {
        TusharePro pro = new TusharePro("xxx");
        Table table = pro.indexBasic();
        System.out.println(table.print());
    }
}
