package com.github.superzhc.db.influxdb.data;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/2/24 16:25
 **/
public class FundETFRealTime {
    public static void main(String[] args) {
        AKTools akTools = new AKTools("127.0.0.1", 8080);
        String measurement = "fund_etf_spot_em";

        List<Map<String, Object>> data = akTools.get(measurement);
        System.out.println(MapUtils.print(data));
    }
}
