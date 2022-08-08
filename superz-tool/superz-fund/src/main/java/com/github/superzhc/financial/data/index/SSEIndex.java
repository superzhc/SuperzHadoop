package com.github.superzhc.financial.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.HttpConstant;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上海证券交易所
 *
 * @author superz
 * @create 2022/5/13 20:32
 */
public class SSEIndex {
    public static Table indices() {
        String url = "http://query.sse.com.cn/commonSoaQuery.do";

        Map<String, Object> params = new HashMap<>();
        params.put("jsonCallBack", String.format("jQuery112403500925340824117_%d", System.currentTimeMillis()));
        params.put("isPagination", false);
        params.put("sqlId", "DB_SZZSLB_ZSLB");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params)
                .userAgent(HttpConstant.UA)
                .referer("http://www.sse.com.cn/")
                .body();

        result = result.substring(result.indexOf("({") + 1, result.length() - 1);
        JsonNode json = JsonUtils.json(result, "pageHelp", "data");

        List<String> columnNames = //JsonUtils.extractObjectColumnName(json)
                Arrays.asList(
                        "handbookUrl",
                        "nIndexFullNameEn",
                        "nIndexNameEn",
                        "tIndexCode",
                        "nIndexCode",
                        "indexReleaseChannel",
                        "indexCode",
                        "issueVolumn",
                        "introEn",
                        "indexFullName",
                        "nIndexFullName",
                        "totalReturnIntro",
                        "tIndexNameEn",
                        "indexFullNameEn",
                        "isNetLncomeIndex",
                        "intro",
                        "tIndexFullName",
                        "indexBaseDay",
                        "ifIndexCode",
                        "numOfStockes",
                        "netReturnIntroEn",
                        "indexBasePoint",
                        "indicsSeqDescEn",
                        "indexName",
                        "netReturnIntro",
                        "isPriceIndex",
                        "updateTime",
                        "indicsSeqDesc",
                        "indexDataSourceType",
                        "launchDay",
                        "methodologyNameEn",
                        "methodologyName",
                        "tIndexFullNameEn",
                        "handbookEnUrl",
                        "indicsSeq",
                        "nIndexName",
                        "indexNameEn",
                        "totalReturnIntroEn",
                        "tIndexName",
                        "isTotalReturnIndex"
                );
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table index(String symbol) {
        String url = "http://query.sse.com.cn/commonSoaQuery.do";

        Map<String, Object> params = new HashMap<>();
        params.put("isPagination", false);
        params.put("sqlId", "DB_SZZSLB_ZSLB");
        params.put("indexCode", "000001");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(HttpConstant.UA).referer("http://www.sse.com.cn/").body();
        JsonNode json = JsonUtils.json(result, "result");

        List<String> columnNames = Arrays.asList("handbookUrl", "nIndexFullNameEn", "nIndexNameEn", "tIndexCode", "nIndexCode", "indexReleaseChannel", "indexCode", "issueVolumn", "introEn", "indexFullName", "nIndexFullName", "totalReturnIntro", "tIndexNameEn", "indexFullNameEn", "isNetLncomeIndex", "intro", "tIndexFullName", "indexBaseDay", "ifIndexCode", "numOfStockes", "netReturnIntroEn", "indexBasePoint", "indicsSeqDescEn", "indexName", "netReturnIntro", "isPriceIndex", "updateTime", "indicsSeqDesc", "indexDataSourceType", "launchDay", "methodologyNameEn", "methodologyName", "tIndexFullNameEn", "handbookEnUrl", "indicsSeq", "nIndexName", "indexNameEn", "totalReturnIntroEn", "tIndexName", "isTotalReturnIndex");
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = Table.create();

        // table = indices();
        table = index("000001.SH");
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
        System.out.println(table.structure().stringColumn(1).map(d -> String.format("\"%s\"", d)).asList());
    }
}
