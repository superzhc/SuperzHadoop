package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

/**
 * @author superz
 * @create 2022/4/26 1:15
 */
public class ENiuIndex {
    public static Table history3M(String indexCode) {
        return history(indexCode, "3");
    }

    public static Table history6M(String indexCode) {
        return history(indexCode, "6");
    }

    public static Table history1Y(String indexCode) {
        return history(indexCode, "12");
    }

    public static Table history5Y(String indexCode) {
        return history(indexCode, "60");
    }

    public static Table history10Y(String indexCode) {
        return history(indexCode, "120");
    }

    /**
     * @param indexCode 示例：000016.SH
     *
     * @return
     */
    public static Table history(String indexCode) {
        return history(indexCode, "all");
    }

    private static Table history(String indexCode, String type) {
        String url = String.format("https://eniu.com/chart/peindex/%s/t/%s", transform(indexCode), type);
        String result = HttpRequest.get(url).body();

        JsonNode json = JsonUtils.json(result);
        JsonNode date = json.get("date");
        JsonNode pe = json.get("pe");
        JsonNode close = json.get("close");

        if (date.size() != pe.size() && date.size() != close.size()) {
            throw new RuntimeException("column[date,pe,close] not match");
        }

        DateColumn dateColumn = DateColumn.create("date");
        DoubleColumn peColumn = DoubleColumn.create("pe");
        DoubleColumn closeColumn = DoubleColumn.create("close");
        for (int i = 0, size = date.size(); i < size; i++) {
            dateColumn.append(LocalDate.parse(date.get(i).asText()));
            peColumn.append(pe.get(i).asDouble());
            closeColumn.append(close.get(i).asDouble());
        }

        Table table = Table.create(dateColumn, peColumn, closeColumn);

        return table;
    }

    private static String transform(String indexCode) {
        String[] ss = indexCode.split("\\.");
        return String.format("%s%s", ss[1].toLowerCase(), ss[0]);
    }
}
