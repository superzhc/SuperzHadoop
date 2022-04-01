package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/4/1 17:16
 **/
public class DoctorXiong {
    private static final Logger log = LoggerFactory.getLogger(DoctorXiong.class);

    private static final String BASE_URL = "https://api.doctorxiong.club";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Table detail(String code) {
        String url = url("/v1/fund/detail");

        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        try {
            String result = HttpRequest.get(url, params).body();
            JsonNode json = mapper.readTree(result);

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
            return table;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Table.create();
    }

    private static String url(String path) {
        return String.format("%s%s", BASE_URL, path);
    }

    public static void main(String[] args) {
        Table table = detail("000001");

        System.out.println(table.structure().printAll());

        table=table.where(table.column("note").isNotMissing());

        System.out.println(table.print());
    }
}
