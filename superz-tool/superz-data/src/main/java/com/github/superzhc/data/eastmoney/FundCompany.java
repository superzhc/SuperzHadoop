package com.github.superzhc.data.eastmoney;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2022/1/11 19:48
 */
public class FundCompany extends HttpData {

    private static final String URL_TEMPLATE = "http://fund.eastmoney.com/Data/FundRankScale.aspx?_=%d";

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public List<List<Object>> get() {
        String url = String.format(URL_TEMPLATE, System.currentTimeMillis());
        ResultT resultT = get(url);
        String result = (String) resultT.getData();
        // String jsonStr = (result).substring("var json={datas:".length(), result.length() - 1);
        String jsonStr = (result).substring("var json=".length());
        try {
            JsonNode json = mapper.readTree(jsonStr);
            json = json.get("datas");

            List<List<Object>> values = new ArrayList<>();
            // ['80000080','山西证券股份有限公司','1988-07-28','16','王怡里','SXZQ','','85.97','★★★','山西证券','','2021/3/31 0:00:00']
            for (JsonNode item : json) {
                List<Object> value = new ArrayList<>();
                for (JsonNode element : item) {
                    value.add(element.asText());
                }
                values.add(value);
            }
            return values;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            List<List<Object>> companies = new FundCompany().get();

            List<List<Object>> addCompanies = new ArrayList<>();
            List<List<Object>> updateCompanies = new ArrayList<>();
            for (List<Object> company : companies) {
                Long count = jdbc.count("fund_company","uid", company.get(0));
                if (count == 0) {
                    addCompanies.add(company);
                } else {
                    company.add(company.get(0));
                    updateCompanies.add(company);
                }
            }

            if (addCompanies.size() > 0) {
                jdbc.batchUpdate("fund_company", "uid,company_name,company_fund_date,manager_crew_num,company_boss_name,company_pinyin,others1,manage_scale,company_rank,company_short_name,others2,update_time", addCompanies);
            }

            if(updateCompanies.size()>0){
                jdbc.batchUpdate("UPDATE fund_company SET uid=?,company_name=?,company_fund_date=?,manager_crew_num=?,company_boss_name=?,company_pinyin=?,others1=?,manage_scale=?,company_rank=?,company_short_name=?,others2=?,update_time=? WHERE uid=?",updateCompanies);
            }
        }
    }
}
