package com.github.superzhc.data.others;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * github 项目地址：https://github.com/fundvis/fund-data
 *
 * @author superz
 * @create 2022/1/12 16:35
 */
public class Fundvis extends HttpData {
    private static final Logger log = LoggerFactory.getLogger(Fundvis.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public Map<String, Map<String, Object>> fund(LocalDate date, String... codes) {
        try {
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            String url = String.format("https://raw.githubusercontent.com/fundvis/fund-data/master/%d/%02d/%02d/all.json", year, month, day);
            ResultT rt = get(url);

            if (rt.getCode() != 0) {
                return null;
            }

            String data = (String) rt.getData();
            if (null == data || data.length() == 0) {
                log.debug("无数据");
                return null;
            }

            JsonNode json = mapper.readTree(data);
            if (json.size() == 0) {
                log.debug("无数据");
                return null;
            }

            Map<String, Map<String, Object>> result = new HashMap<>();
            for (JsonNode fund : json) {
                String fundCode = fund.get("code").asText();
                for (String code : codes) {
                    if (code.trim().equalsIgnoreCase(fundCode)) {
                        Map<String, Object> map = mapper.convertValue(fund, new TypeReference<Map<String, Object>>() {
                            @Override
                            public Type getType() {
                                return super.getType();
                            }
                        });
                        result.put(code, map);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            log.error("解析数据异常", e);
            return null;
        }
    }

    public List<List<Object>> fundFact(LocalDate date) {
        try {
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            String url = String.format("https://raw.githubusercontent.com/fundvis/fund-data/master/%d/%02d/%02d/all.json", year, month, day);
            ResultT rt = get(url);

            if (rt.getCode() != 0) {
                return null;
            }

            String data = (String) rt.getData();
            if (null == data || data.length() == 0) {
                log.debug("无数据");
                return null;
            }

            JsonNode json = mapper.readTree(data);
            if (json.size() == 0) {
                log.debug("无数据");
                return null;
            } else {
                log.debug("Preview : " + mapper.writeValueAsString(json.get(0)));
            }

            int num = json.size();
            // Object[][] values = new Object[num][];
            List<List<Object>> values = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                JsonNode fund = json.get(i);

                String code = fund.get("code").asText();
                String riqi = fund.get("day").asText();
                String unitNetWorth = fund.get("unitNetWorth").asText();

                // 这判定会造成数据非定长的，数组不好使用
                if (StringUtils.isBlank(code) || StringUtils.isBlank(riqi) || StringUtils.isBlank(unitNetWorth)) {
                    continue;
                }

                // Object[] value = new Object[3];
                // value[0] = code;
                // value[1] = riqi;
                // value[2] = Double.parseDouble(unitNetWorth);
                // values[i] = value;
                List<Object> value = new ArrayList<>();
                value.add(code);
                value.add(riqi);
                value.add(Double.parseDouble(unitNetWorth));
                values.add(value);
            }
            return values;
        } catch (Exception e) {
            log.error("解析数据异常", e);
            return null;
        }
    }

    /**
     * 20180812 之前的数据
     *
     * @param code
     * @return
     */
    public Object[][] fundFact20180812(String code) {
        try {
            String url = String.format("https://raw.githubusercontent.com/fundvis/fund-data/master/funds/%s.json", code);
            ResultT rt = get(url);
            if (0 != rt.getCode()) {
                return null;
            }

            JsonNode json = mapper.readTree((String) rt.getData());

            if (json.size() == 0) {
                return null;
            }

            JsonNode netValues = json.get(0).get("netvalues");

            int num = netValues.size();
            Object[][] values = new Object[num][];
            for (int i = 0; i < num; i++) {
                JsonNode netValue = netValues.get(i);

                Object[] value = new Object[5];
                value[0] = code;
                // riqi
                value[1] = netValue.get(0).asText();
                // unit_net_worth
                value[2] = netValue.get(1).asDouble();
                // accumulate_net_worth
                value[3] = netValue.get(2).asDouble();
                //net_worth_growth
                String growth = netValue.get(3).asText();
                if (null == growth || growth.trim().length() == 0) {
                    value[4] = 0.0;
                } else {
                    value[4] = Double.parseDouble(growth.substring(0, growth.length() - 1));
                }

                values[i] = value;
            }

            return values;
        } catch (Exception e) {
            log.error("解析数据异常", e);
            return null;
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        Fundvis fundvis = new Fundvis();


        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            // 注释代码为初始化全量获取数据
//            List<Map<String, Object>> funds = jdbc.select("fund_dimension", "code");
//            for (Map<String, Object> fund : funds) {
//                Object[][] fundNetWorthValues = fundvis.fundFact20180812((String) fund.get("code"));
//                jdbc.batchUpdate("fund_fact2", "code,riqi,unit_net_worth,accumulate_net_worth,net_worth_growth", fundNetWorthValues, 1000);
//            }
//            LocalDate start = LocalDate.of(2018, 8, 14);
//            LocalDate end = LocalDate.now();
//            while (start.isBefore(end)) {
//                List<List<Object>> fundsNetWorth = fundvis.fundFact(start);
//                jdbc.batchUpdate("fund_fact2", "code,riqi,unit_net_worth", fundsNetWorth, 1000);
//
//                start = start.plusDays(1);
//            }

            // 如下为增量获取数据
            LocalDate today = LocalDate.now();
            List<List<Object>> fundsNetWorth = fundvis.fundFact(today.minusDays(1));
            //System.out.println(fundsNetWorth);
            jdbc.batchUpdate("fund_fact2", "code,riqi,unit_net_worth", fundsNetWorth, 1000);
        }
    }
}
