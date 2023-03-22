package com.github.superzhc.db.influxdb.project;

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
        RestApi influxApi = new RestApi("127.0.0.1", 8086);
        AKTools akTools = new AKTools("127.0.0.1", 8080);

        String api = "fund_etf_hist_min_em";
        String code = "516510";

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", code);
        params.put("start_date", "2023-02-27 08:30:00");
        params.put("end_date", "2023-03-22 16:00:00");
        params.put("period", "30");
        params.put("adjust", "");

        List<Map<String, Object>> data = akTools.get(api, params);
        System.out.println(MapUtils.print(data));

        List<LineProtocol> lineProtocols = new ArrayList<>();
        for (Map<String, Object> item : data) {
            LocalDateTime dateTime = LocalDateTime.parse(item.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            LineProtocol lineProtocol = LineProtocol.builder()
                    .measurement(api)
                    .addTag("code",code)
                    .addField("volume", item.get("volume"))
                    .addField("account", item.get("account"))
                    .timestamp(dateTime)
                    .build();
            lineProtocol.setMeasurement(api);
        }

        influxApi.writeBatch("xgit", lineProtocols);
    }
}
