package com.github.superzhc.financial.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/20 14:01
 **/
public class DanJuanIndex {

    private static final Logger log = LoggerFactory.getLogger(DanJuanIndex.class);

    /**
     * 指数估值
     *
     * @return
     */
    public static Table indexEva() {
        List<Map<String, Object>> data = com.github.superzhc.data.index.DanJuanIndex.indexEva();
        Table table = TableUtils.buildByMap(data);
        return table;
    }

    /**
     * 单个指数估值
     *
     * @param indexCode 示例：000905.SH
     *
     * @return
     */
    public static Table indexEva(String indexCode) {
        Map<String, Object> map = com.github.superzhc.data.index.DanJuanIndex.indexEva(indexCode);
        Table table = TableUtils.map2Table(map);
        return table;
    }

    /**
     * 指数历史
     *
     * @param indexCode
     * @param type 数据量，可选值：1y,3y,5y,10y
     *
     * @return
     */
    public static Table indexHistory(String indexCode, String type) {
        List<Map<String, Object>> data = com.github.superzhc.data.index.DanJuanIndex.indexHistory(indexCode, type);
        Table table = TableUtils.buildByMap(data);
        return table;
    }

    public static Table indexValue(String indexCode, String type) {
        Table peT = peHistory(indexCode, type);
        Table pbT = pbHistory(indexCode, type);
        // Table roeT = roeHistory(indexCode, type);

        if (peT.rowCount() != pbT.rowCount()
            // || peT.rowCount() != roeT.rowCount()
        ) {
            log.warn("pb:{},pe:{},roe:{}", pbT.rowCount(), peT.rowCount()/*, roeT.rowCount()*/);
        }

        Table table = peT.joinOn("date").inner(pbT/*, roeT*/);
        return table;
    }

    public static Table peHistory3Y(String indexCode) {
        return peHistory(indexCode, "3y");
    }

    public static Table peHistroy5Y(String indexCode) {
        return peHistory(indexCode, "5y");
    }

    public static Table peHistory10Y(String indexCode) {
        // 实质也只有是十年数据
        return peHistory(indexCode, "all");
    }

    private static Table peHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/index_eva/pe_history/%s?day=%s", transform(indexCode), type);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "index_eva_pe_growths");

        List<String> columnNames = Arrays.asList(
                "pe",
                "ts"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.replaceColumn("ts", table.longColumn("ts").asDateTimes(ZoneOffset.ofHours(+8)).date().setName("date"));

        return table;
    }

    public static Table pbHistory3Y(String indexCode) {
        return pbHistory(indexCode, "3y");
    }

    public static Table pbHistroy5Y(String indexCode) {
        return pbHistory(indexCode, "5y");
    }

    public static Table pbHistory10Y(String indexCode) {
        return pbHistory(indexCode, "all");
    }

    public static Table pbHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/index_eva/pb_history/%s?day=%s", transform(indexCode), type);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "index_eva_pb_growths");

        List<String> columnNames = Arrays.asList(
                "pb",
                "ts"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.replaceColumn("ts", table.longColumn("ts").asDateTimes(ZoneOffset.ofHours(+8)).date().setName("date"));

        return table;
    }

    public static Table roeHistory3Y(String indexCode) {
        return roeHistory(indexCode, "3y");
    }

    public static Table roeHistroy5Y(String indexCode) {
        return roeHistory(indexCode, "5y");
    }

    public static Table roeHistory10Y(String indexCode) {
        return roeHistory(indexCode, "all");
    }

    public static Table roeHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/index_eva/roe_history/%s?day=%s", transform(indexCode), type);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "index_eva_roe_growths");

        List<String> columnNames = Arrays.asList(
                "roe",
                "ts"
        );

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table.replaceColumn("ts", table.longColumn("ts").asDateTimes(ZoneOffset.ofHours(+8)).date().setName("date"));

        return table;
    }

    /**
     * 转换成蛋卷的指数表示方式
     *
     * @param indexCode 示例 000905.SH
     *
     * @return
     */
    private static String transform(String indexCode) {
        String[] ss = indexCode.split("\\.");
        return String.format("%s%s", ss[1], ss[0]);
    }

    public static void main(String[] args) {
        Table table = indexEva();
        System.out.println(table.structure().printAll());
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
