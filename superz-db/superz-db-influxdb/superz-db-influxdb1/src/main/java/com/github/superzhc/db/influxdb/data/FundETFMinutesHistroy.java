package com.github.superzhc.db.influxdb.data;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.db.influxdb.LineProtocol;
import com.github.superzhc.db.influxdb.RestApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/2/24 15:59
 **/
public class FundETFMinutesHistroy {
    public static void main(String[] args) {
        RestApi influxApi = new RestApi("127.0.0.1", 8086, true);
        AKTools akTools = new AKTools("127.0.0.1", 8080);

        String api = "fund_etf_hist_min_em";
        String code = "516510";

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", code);
        params.put("start_date", "2023-01-01 08:30:00");
        params.put("end_date", "2023-02-24 16:00:00");
        params.put("period", "30");
        params.put("adjust", "");

        List<Map<String, Object>> data = akTools.get(api, params);
        // System.out.println(MapUtils.print(data));

        List<LineProtocol> lineProtocols = new ArrayList<>();
        for (Map<String, Object> item : data) {
            LineProtocol lineProtocol = new LineProtocol();
            lineProtocol.setMeasurement(api);

            Map<String, String> tags = new HashMap<>();
            tags.put("code", code);
            lineProtocol.setTagSet(tags);

            Map<String, Object> fields = new HashMap<>();
            fields.put("volume", item.get("成交量"));
            fields.put("account", item.get("成交额"));
            lineProtocol.setFieldSet(fields);

            LocalDateTime dateTime = LocalDateTime.parse(item.get("时间").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            lineProtocol.setTimestamp(dateTime);

            lineProtocols.add(lineProtocol);
        }
//        System.out.println(LineProtocol.buildBatch(lineProtocols));

        System.out.println(influxApi.writeBatch("xgit", lineProtocols));
    }
}
