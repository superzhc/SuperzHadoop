package com.github.superzhc.db.influxdb.project;

import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.db.influxdb.LineProtocol;
import com.github.superzhc.db.influxdb.RestApi;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/24 15:17
 **/
public class LocalInflxuDB {
    RestApi api;
    AKTools dataApi;

    @Before
    public void setUp() {
        api = new RestApi("127.0.0.1", 8086);
        dataApi = new AKTools("127.0.0.1");
    }

    @Test
    public void ping() {
        System.out.println(api.ping());
    }

    @Test
    // 查看当前有哪些数据库
    public void databases() {
        System.out.println(ListUtils.list2String(api.databases(), ","));
    }

    @Test
    // 创建数据库
    public void createDatabase() {
        api.createDB("test");
    }

    @Test
    // 查看指定库下有哪些measurement
    public void measurements() {
        System.out.println(ListUtils.list2String(api.measurements("test"), ","));
    }

    @Test
    public void writeIndexDailyData() {
        String code = "sh000004";
        List<Map<String, Object>> data = dataApi.get("stock_zh_index_daily", Collections.singletonMap("symbol", code));

        String measurement = "index_daily_data";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        List<LineProtocol> lineProtocols = new ArrayList<>();
        for (Map<String, Object> item : data) {
            LocalDateTime dt = LocalDateTime.parse(String.valueOf(item.get("date")), formatter);

            LineProtocol lineProtocol = LineProtocol.builder()
                    .measurement(measurement)
                    .addTag("tc", code.substring(2))
                    .addField("fc", item.get("close"))
                    .addField("fv", item.get("volume"))
                    .timestamp(dt)
                    .build();
            lineProtocols.add(lineProtocol);
        }

        api.writeBatch("test", lineProtocols);
    }

    @Test
    public void readIndexDailyData() {
        String measurement = "index_daily_data";

        String influxQL = "select * from " + measurement + " LIMIT 1000";
        List<Map<String, Object>> data = api.read("test", influxQL);
        MapUtils.show(data);
    }
}
