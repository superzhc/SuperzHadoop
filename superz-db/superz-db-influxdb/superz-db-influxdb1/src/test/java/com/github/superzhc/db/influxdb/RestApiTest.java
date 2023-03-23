package com.github.superzhc.db.influxdb;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;

public class RestApiTest {

    private RestApi api;
    /*数据接口类*/
    private AKTools akTools;

    @Before
    public void setUp() throws Exception {
//        api = new RestApi("127.0.0.1", 8086);
        api = new RestApi("10.90.18.100", 8086);
        api.enableDebug();
        akTools = new AKTools("127.0.0.1", 8080);
    }

    @Test
    public void ping() {
        System.out.println(api.ping());
    }

    @Test
    public void createDB() {
        api.createDB("superz");
    }

    @Test
    public void testDropDB() {
        api.dropDB("superz");
    }

    @Test
    public void databases() {
        System.out.println(api.databases());
    }

//    @Test
//    public void retentionPolicies() {
//        String result = api.retentionPolicies("xgit");
//        System.out.println(result);
//    }

    @Test
    public void series() {
        System.out.println(api.series("superz"));
    }

    @Test
    public void measurements() {
        System.out.println(api.measurements("superz"));
    }

    @Test
    public void testDropMeasurement() {
        api.dropMeasurement("superz", "spot_hist_sge");
    }

    @Test
    public void tagKeys() {
        System.out.println(api.tagKeys("superz"));
    }

    @Test
    public void tagKeys2() {
        List<String> data = api.tagKeys("superz", "spot_hist_sge");
        System.out.println(data);
    }

    @Test
    public void fieldKeys() {
        List<String> result = api.fieldKeys("superz", "spot_hist_sge");
        System.out.println(result);
    }

    @Test
    public void write() {
        String influxQLTemp = "test%d,tag1=k%d,tag2=k%d field1=%f,field2=%f";
        for (int i = 1; i <= 10; i++) {
            String influxQL = String.format(influxQLTemp, i, i, i, i * 1.0, i * 2.0);
            api.write("front", influxQL);
        }
    }

    @Test
    public void spot_hist_sge() {
        String measurement = "spot_hist_sge";

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", "Au99.99");

        List<Map<String, Object>> data = akTools.get(measurement);
        //System.out.println(MapUtils.print(data));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        List<LineProtocol> lineProtocols = new ArrayList<>();
        for (Map<String, Object> item : data) {
            LocalDateTime dateTime = LocalDateTime.parse(String.valueOf(item.get("date")), formatter);
            item.remove("date");

            LineProtocol protocol = LineProtocol.builder()
                    .measurement(measurement)
                    .addTag("type", "d1")
                    .fields(item)
                    .timestamp(dateTime)
                    .build();

            lineProtocols.add(protocol);
        }

        api.writeBatch("superz", lineProtocols);
    }

    @Test
    public void read() {
        String influxQL = "select * from spot_hist_sge";
        List<Map<String, Object>> result = api.read("superz", influxQL);
        System.out.println(MapUtils.print(result));
    }

    @Test
    public void test3() {
        String influxQL = "select max(account),max(volume),min(account),min(volume) from fund_etf_hist_min_em group by code";
        List<Map<String, Object>> result = api.read("xgit", influxQL);
        System.out.println(MapUtils.print(result));
    }

    @Test
    public void test4() {
        String influxQL = "select * from fund_etf_hist_min_em where time >= '2023-01-01T00:00:00Z'";
        List<Map<String, Object>> result = api.read("xgit", influxQL);
        System.out.println(MapUtils.print(result));
    }

    @Test
    public void testPage() {
        String influxQL = "select * from index_zh_spot limit 100 offset 100";
        List<Map<String, Object>> data = api.read("superz", influxQL);
        MapUtils.show(data);
    }

    @Test
    public void testCount() {
        String influxQL = "select count(*) from index_zh_spot";
        String result = api.query("superz", influxQL);
        System.out.println(result);
    }
}