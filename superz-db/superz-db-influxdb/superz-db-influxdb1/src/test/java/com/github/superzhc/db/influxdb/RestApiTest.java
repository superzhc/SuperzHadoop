package com.github.superzhc.db.influxdb;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RestApiTest {

    private RestApi api;
    /*数据接口类*/
    private AKTools akTools;

    @Before
    public void setUp() throws Exception {
        api = new RestApi("127.0.0.1", 8086);
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
    public void databases() {
        String result = api.databases();
        JsonNode json = JsonUtils.json(result, "results", 0, "series", 0, "values");
        System.out.println(JsonUtils.newArrayArray(json));
    }

    @Test
    public void retentionPolicies() {
        String result = api.retentionPolicies("xgit");
        System.out.println(result);
    }

    @Test
    public void series() {
        String result = api.series("xgit");
        System.out.println(result);
    }

    @Test
    public void measurements() {
        String result = api.measurements("xgit");
        System.out.println(result);
    }

    @Test
    public void tagKeys() {
        String result = api.tagKeys("xgit");
        System.out.println(result);
    }

    @Test
    public void tagKeys2() {
        String result = api.tagKeys("xgit", "fund_etf_hist_min_em");
        System.out.println(result);
    }

    @Test
    public void fieldKeys(){
        String result = api.fieldKeys("xgit", "fund_etf_hist_min_em");
        System.out.println(result);
    }

    @Test
    public void write() {
        String ql = "cpu_load_short,host=server03,region=us-west value1=0.64 1434055564000000000";
        api.write("xgit", ql);
    }

    @Test
    public void writeBatch() {
        String measurement = "fund_etf_spot_em";
        List<Map<String, Object>> data = akTools.get(measurement);

        List<LineProtocol> lineProtocols = new ArrayList<>();
        for (Map<String, Object> item : data) {
            LineProtocol protocol = new LineProtocol();
            protocol.setMeasurement(measurement);

            Map<String, String> tags = new HashMap<>();
            tags.put("code", item.get("代码").toString());
            tags.put("name", item.get("名称").toString());
            protocol.setTagSet(tags);

            Map<String, Object> fields = new HashMap<>();
            fields.put("last_close", item.get("昨收"));
            fields.put("open", item.get("开盘价"));
            fields.put("high", item.get("最高价"));
            fields.put("low", item.get("最低价"));
            fields.put("new", item.get("最新价"));
            fields.put("change", item.get("涨跌额"));
            fields.put("change_per", item.get("涨跌幅"));
            protocol.setFieldSet(fields);

            lineProtocols.add(protocol);
        }

        System.out.println(api.writeBatch("xgit", lineProtocols));
    }

    @Test
    public void read() {
        String influxQL = "select * from cpu_load_short where host='server03'";
        String result = api.read("xgit", influxQL);
        System.out.println(result);
        JsonNode json = JsonUtils.json(result, "results", 0, "series", 0);
        String[] columns = JsonUtils.stringArray(json, "columns");
        Object[][] data = JsonUtils.newArrayArray(json, "values");
        System.out.println(ListUtils.print(columns, data));
    }

    @Test
    public void read2() {
        String influxQL = "select * from cpu_load_short";
        String result = api.read("xgit", influxQL);
        System.out.println(result);
        JsonNode json = JsonUtils.json(result, "results", 0, "series", 0);
        String[] columns = JsonUtils.stringArray(json, "columns");
        Object[][] data = JsonUtils.newArrayArray(json, "values");
        System.out.println(ListUtils.print(columns, data));
    }

    @Test
    public void test3() {
        String influxQL = "select max(account),max(volume),min(account),min(volume) from fund_etf_hist_min_em group by code";
        String result = api.read("xgit", influxQL);
        JsonNode json = JsonUtils.json(result, "results", 0, "series", 0);
        String[] columns = JsonUtils.stringArray(json, "columns");
        Object[][] data = JsonUtils.newArrayArray(json, "values");
        System.out.println(ListUtils.print(columns, data));
    }

    @Test
    public void test4() {
        String influxQL = "select * from fund_etf_hist_min_em where time >= '2023-01-01T00:00:00Z'";
        String result = api.read("xgit", influxQL);
        System.out.println(result);
        JsonNode json = JsonUtils.json(result, "results", 0, "series", 0);
        String[] columns = JsonUtils.stringArray(json, "columns");
        Object[][] data = JsonUtils.newArrayArray(json, "values");
        System.out.println(ListUtils.print(columns, data));
    }
}