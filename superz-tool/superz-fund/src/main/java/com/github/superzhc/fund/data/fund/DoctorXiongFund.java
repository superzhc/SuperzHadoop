package com.github.superzhc.fund.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/4/1 17:16
 **/
public class DoctorXiongFund {
    private static final Logger log = LoggerFactory.getLogger(DoctorXiongFund.class);

    private static final String BASE_URL = "https://api.doctorxiong.club";

    public static Table funds(String... codes) {
        String url = url("/v1/fund/detail/list");

        Map<String, Object> params = new HashMap<>();
        params.put("code", String.join(",", codes));
        params.put("startDate", "1990-01-01");

        String result = HttpRequest.get(url, params).acceptGzipEncoding().body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = Arrays.asList(
                "code",
                "name",
                "type",
                "netWorth",
                "netWorthDate",
                "expectWorth",
                "expectGrowth",
                "expectWorthDate",
                "totalWorth",
                "dayGrowth",
                "lastWeekGrowth",
                "lastMonthGrowth",
                "lastThreeMonthsGrowth",
                "lastSixMonthsGrowth",
                "lastYearGrowth",
                "buyMin",
                "buySourceRate",
                "buyRate",
                "manager",
                "fundScale"
        );
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table fundWorth(String code) {
        String url = url("/v1/fund/detail");

        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result);

        String tableName = String.format("%s[code=%s,type=%s,manager=%s](%s)"
                , JsonUtils.string(json,"data","name")
                , JsonUtils.string(json,"data","code")
                , JsonUtils.string(json,"data","type")
                , JsonUtils.string(json,"data","manager")
                , JsonUtils.string(json,"data","netWorthDate")
        );

        EmptyReadOptions options = EmptyReadOptions.builder().build();

        // ["2001-12-18", "1.0", "0", "描述"]
        List<String> netWorthColumnNames = Arrays.asList("day", "net_worth", "increase", "note");
        List<String[]> netWorthTableData = new ArrayList<>();
        // Table netWorthTable
        JsonNode netWorthData = json.get("data").get("netWorthData");
        for (JsonNode item : netWorthData) {
            String[] row = new String[item.size()];
            for (int i = 0, len = item.size(); i < len; i++) {
                row[i] = item.get(i).asText();
            }
            netWorthTableData.add(row);
        }
        Table netWorthTable = TableBuildingUtils.build(netWorthColumnNames, netWorthTableData, options);

        // ["2001-12-18", "1.0"]
        List<String> totalNetWorthColumnNames = Arrays.asList("day", "total_net_worth");
        List<String[]> totalNetWorthTableData = new ArrayList<>();
        JsonNode totalNetWorthData = json.get("data").get("totalNetWorthData");
        for (JsonNode item : totalNetWorthData) {
            String[] row = new String[item.size()];
            for (int i = 0, len = item.size(); i < len; i++) {
                row[i] = item.get(i).asText();
            }
            totalNetWorthTableData.add(row);
        }
        Table totalNetWorthTable = TableBuildingUtils.build(totalNetWorthColumnNames, totalNetWorthTableData, options);

        Table table = netWorthTable.joinOn("day").inner(totalNetWorthTable);
        table = table.reorderColumns("day", "net_worth", "total_net_worth", "increase", "note");
        table.setName(tableName);
        return table;
    }

    private static String url(String path) {
        return String.format("%s%s", BASE_URL, path);
    }

    public static void main(String[] args) {
        Table table = funds("501009");//fundWorth("501009");
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
