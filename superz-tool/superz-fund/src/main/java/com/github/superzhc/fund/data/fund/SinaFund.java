package com.github.superzhc.fund.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2022/4/6 13:53
 **/
public class SinaFund {

    public static DateTimeFormatter DEFAULT_DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Table colsedFund() {
        return innerFund("close_fund");
    }

    public static Table etf() {
        return innerFund("etf_hq_fund");
    }

    public static Table lof() {
        return innerFund("lof_hq_fund");
    }

    private static Table innerFund(String type) {
        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/jsonp.php/IO.XSRV2.CallbackList['da_yPT46_Ll7K6WD']/Market_Center.getHQNodeDataSimple";

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("num", "1000");
        params.put("sort", "symbol");
        params.put("asc", "0");
        params.put("node", type);
        params.put("[object HTMLDivElement]", "qvvne");

        String result = HttpRequest.get(url, params).body();
        result = result.substring(result.indexOf("([") + 1, result.length() - 2);

        JsonNode json = JsonUtils.json(result);
        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        // 统一编码的规则
        StringColumn column = table.stringColumn("symbol").map(d -> {
            int length = d.length();
            String market = d.substring(0, length - 6).toUpperCase();
            String code = d.substring(length - 6);
            return String.format("%s.%s", code, market);
        });

        table.replaceColumn("symbol", column);

        return table;
    }

    public static void main(String[] args) throws Exception {
        Table table = etf();

        System.out.println(table.print());
        //System.out.println(table.structure().printAll());
        //System.out.println(table.shape());
        //System.out.println(table.summarize("close", max).apply().printAll());
    }
}
