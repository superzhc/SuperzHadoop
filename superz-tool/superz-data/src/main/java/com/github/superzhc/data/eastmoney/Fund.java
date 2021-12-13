package com.github.superzhc.data.eastmoney;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2021/10/29 14:10
 */
public class Fund extends HttpData {
    private static final String URL = "http://fund.eastmoney.com";

    private static final String COMPANY_URL = URL + "/js/jjjz_gs.js?dt=";
    private static final String FUNDCODE_URL = URL + "/js/fundcode_search.js";

    private static String[] fund_attributes = {"基金代码", "基金简拼", "基金名称", "基金类型", "基金全拼"};

    private ObjectMapper mapper;


    public Fund() {
        this.mapper = new ObjectMapper();
    }

    public void company() {
        String result = (String) get(FUNDCODE_URL).getData();
        result.substring("var gs=".length());
    }

    public String fundcode() {
        try {
            String result = (String) get(FUNDCODE_URL).getData();
            String data = result.substring("var r = ".length(), result.length() - 1);
            JsonNode funds = mapper.readTree(data);

            ArrayNode fundsJson = mapper.createArrayNode();

            for (JsonNode fund : funds) {

                ObjectNode fundJson = mapper.createObjectNode();

                for (int i = 0, len = fund_attributes.length; i < len; i++) {
                    fundJson.put(fund_attributes[i], fund.get(i).asText());
                }

                fundsJson.add(fundJson);
            }

            return mapper.writeValueAsString(fundsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 需存在如下表：
     *
     * create table fund_dimension
     * (
     *     id            int auto_increment
     *         primary key,
     *     code          varchar(20)  not null,
     *     name          varchar(255) null,
     *     type          varchar(255) null comment '类型',
     *     simple_pinyin varchar(50)  null comment '简拼',
     *     full_pinyin   varchar(255) null comment '全拼',
     *     constraint fund_dimension_code_uindex
     *         unique (code)
     * )
     *     charset = utf8;
     * @param url
     * @param username
     * @param password
     */
    public void funds2db(String url, String username, String password) {

        try {
            String result = (String) get(FUNDCODE_URL).getData();
            String data = result.substring("var r = ".length(), result.length() - 1);

            List<List<Object>> values = new ArrayList<>();

            JsonNode funds = mapper.readTree(data);
            for (JsonNode fund : funds) {
                List<Object> value = new ArrayList<>();
                for (JsonNode item : fund) {
                    value.add(item.asText());
                }
                values.add(value);
            }

            JdbcHelper jdbc = new JdbcHelper(url, username, password);
            jdbc.batchUpdate("insert into fund_dimension(code,simple_pinyin,name,type,full_pinyin) values(?,?,?,?,?)", values, 100);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        Fund fund = new Fund();
        fund.funds2db("jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8", "root", "123456");
        System.out.println("插入成功");
    }
}