package com.github.superzhc.fund.fund;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.data.eastmoney.FundBasic;
import com.github.superzhc.data.others.Fundvis;
import com.github.superzhc.fund.util.FundUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/1/13 13:43
 */
public class FundMain {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        String[] codes = {"164402", "000478", "501009", "160119", "004752", "519915", "501050", "519671", "160716", "000071", "012348", "012820", "001594", "001595", "090010", "006327", "164906"};

        Map<String, Map<String, Object>> funds = new Fundvis().fund(LocalDate.now().minusDays(1), codes);
        for (String code : codes) {
//            Map<String, Object> map = new FundBasic().fund(code);
//            map.put("is_out_the_counter_fund", FundUtils.isOutTheCounterFund(code));
//            System.out.println(mapper.writeValueAsString(map));

            Map<String, Object> map2 = funds.get(code);
            System.out.println(mapper.writeValueAsString(map2));
        }
    }
}
