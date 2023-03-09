package com.github.superzhc.db.influxdb;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InfluxDBClientTest {
    private InfluxDBClient client;

    @Before
    public void setUp() throws Exception {
        client=new InfluxDBClient("http://127.0.0.1:8086");
    }

    @Test
    public void testMeasurements(){
        System.out.println(client.measurements("xgit"));
    }

    @Test
    public void testTags(){
        System.out.println(client.tags("xgit","fund_etf_hist_min_em"));
    }

    @Test
    public void testFields(){
        System.out.println(client.fields("xgit","payment"));
    }

    @Test
    public void testKeys(){
        System.out.println(client.keys("xgit","fund_etf_spot_em"));
    }

    @Test
    public void testQuery(){
        String influxQL="select * from fund_etf_spot_em limit 20";
        System.out.println(client.query("xgit",influxQL));
    }
}