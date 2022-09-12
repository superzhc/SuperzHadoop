package com.github.superzhc.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/7 23:47
 */
public class DoctorXiong {
    private static final String URL = "https://api.doctorxiong.club";

    /**
     * 获取基金基础信息
     *
     * @param codes
     *
     * @return
     */
    public static List<Map<String, String>> fund(String... codes) {
        if (null == codes || codes.length == 0) {
            return null;
        }

        String url = String.format("%s/v1/fund", URL);

        Map<String, String> params = new HashMap<>();
        params.put("code", String.join(",", codes));

        String result = HttpRequest.get(url, params).body();
        JsonNode data = JsonUtils.json(result, "data");
        Map<String, String>[] maps = JsonUtils.objectArray2Map(data);
        return Arrays.asList(maps);
    }

    public static List<Map<String, Object>> fundDetail(String startDate, String endDate, String code) {
        String url = String.format("%s/v1/fund/detail", URL);

        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("code", code);

        String result = HttpRequest.get(url, params).body();
        JsonNode data = JsonUtils.json(result, "data");

        // String code = JsonUtils.string(data, "code");
        String name = JsonUtils.string(data, "name");
        String type = JsonUtils.string(data, "type");

        List<String[]> netWorths = JsonUtils.arrayArray2(data, "netWorthData");
        List<String[]> totalNetWorths = JsonUtils.arrayArray2(data, "totalNetWorthData");

        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0, len = netWorths.size(); i < len; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("code", code);
            map.put("name", name);
            map.put("type", type);

            String[] netWorth = netWorths.get(i);
            map.put("date", netWorth[0]);
            map.put("netWorth", netWorth[1]);
            map.put("change", netWorth[2]);

            String[] totalNetWorth = totalNetWorths.get(i);
            map.put("totalNetWorth", totalNetWorth[1]);

            maps.add(map);
        }

        return maps;
    }

    public static void main(String[] args) {
        String[] codes = new String[]{"000478", "519671"};


    }
}
