package com.github.superzhc.data.other;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AKToolsTest {

    private AKTools instance;

    @Before
    public void setUp() throws Exception {
        instance = new AKTools("127.0.0.1");
    }

    @Test
    public void testGet() {
        Map<String,Object> params=new HashMap<>();
        params.put("date","20230216");

        List<Map<String, Object>> data = instance.get("fund_etf_spot_em");
        System.out.println(MapUtils.print(data));
    }
}