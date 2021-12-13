package com.github.superzhc.data.doctorxiong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.common.ResultT;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 官网：https://www.doctorxiong.club/
 * <p>
 * 注意事项
 * 1. api的调用限制：未经验证的用户每小时限制100次
 *
 * @author superz
 * @create 2021/12/13 14:12
 */
public class DoctorXiong extends HttpData {
    private static final String URL = "https://api.doctorxiong.club";

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper mapper;

    public DoctorXiong() {
        mapper = new ObjectMapper();
    }

    /**
     * 获取基金基础信息
     *
     * @param codes
     * @return
     */
    public ResultT fund(String... codes) {
        if (null == codes || codes.length == 0) {
            return ResultT.fail("fund code must require , more code use comma");
        }

        Map<String, String> params = new HashMap<>();
        params.put("code", String.join(",", codes));
        return get("/v1/fund", null, params);
    }

    /**
     * 批量获取基金详情
     * 详细见：https://www.doctorxiong.club/api/#api-Fund-getFundDetailList
     *
     * @param startDate:开始时间,标准时间格式yyyy-MM-dd
     * @param endDate:截至时间,标准时间格式yyyy-MM-dd
     * @param codes:基金代码
     * @return
     */
    public ResultT fundDetailList(String startDate, String endDate, String... codes) {
        if (null == codes || codes.length == 0) {
            return ResultT.fail("fund code must require , more code use comma");
        }

        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("code", String.join(",", codes));
        return get("/v1/fund/detail/list", null, params);
    }

    /**
     * 获取基金详情
     *
     * @param startDate
     * @param endDate
     * @param code
     * @return
     */
    public ResultT fundDetail(String startDate, String endDate, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("code", code);
        return get("/v1/fund/detail", null, params);
    }

    /**
     * 获取基金持仓详情
     *
     * @param code
     * @return
     */
    public ResultT fundPosition(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        return get("/v1/fund/position", null, params);
    }

    public ResultT fundAll() {
        return fundAll(null);
    }

    public ResultT fundAll(String fuzzyQuery) {
        Map<String, String> params = new HashMap<>();
        if (null != fuzzyQuery && fuzzyQuery.trim().length() > 0) {
            params.put("keyWord", fuzzyQuery);
        }
        return get("/v1/fund/all");
    }

    /**
     * 获取基金排行
     * <p>
     * 根据条件获取基金排行(包含基本基金信息),不合法的参数会被直接忽略
     *
     * @param fundType       基金gp(股票型),hh(混合型),zq(债券型),zs(指数型)(可以接受多个参数)
     *                       默认值: 所有类型
     *                       允许值: {"gp", "hh", "zq", "zs", "qdii", "fof"}
     * @param sort           r(日涨幅)     z(周涨幅),1y(最近一个月涨幅),jn(今年涨幅),1n(近一年涨幅)
     *                       默认值: 日涨幅
     *                       允许值: {"r", "z", "1y", "3y", "6y", "jn", "1n", "2n", "3n"}
     * @param fundCompany    华夏("80000222"), 嘉实("80000223"), 易方达("80000229"),南方("80000220"), 中银("80048752"), 广发("80000248"), 工银("80064225"), 博时("80000226"), 华安("80000228"),汇添富("80053708");(接受多个参数)
     *                       默认值: 所有基金公司
     *                       允许值: {"80000222", "80000223", "80000229", "80000220", "80048752", "80000248", "80064225", "80000226", "80000228", "80053708"}
     * @param creatTimeLimit 基金成立时间限制1:小于一年》2:小于两年.依次类推(非必需)
     *                       默认值: 无限制
     * @param fundScale      基金规模单位亿,10表示10亿以下,100表示100亿以下,1001表示100亿以上(非必需)
     *                       允许值: [10, 100, 1001]
     * @param asc            排序方式0:降序:1升序
     *                       默认值: 0
     *                       允许值: {0，1}
     * @param pageIndex      页码
     *                       默认值: 1
     * @param pageSize       每页条目数
     *                       默认值: 10
     * @return
     */
    public ResultT fundRank(String[] fundType, String sort, String[] fundCompany, Integer creatTimeLimit, Integer fundScale, Integer asc, Integer pageIndex, Integer pageSize) {
        ObjectNode jsonNodes = mapper.createObjectNode();

        if (null != fundType && fundType.length > 0) {
            ArrayNode fundTypeNode = mapper.createArrayNode();
            for (String item : fundType) {
                fundTypeNode.add(item);
            }
            jsonNodes.set("fundType", fundTypeNode);
        }

        if (null != sort && sort.trim().length() > 0) {
            jsonNodes.put("sort", sort);
        }

        if (null != fundCompany && fundCompany.length > 0) {
            ArrayNode fundCompanyNode = mapper.createArrayNode();
            for (String item : fundCompany) {
                fundCompanyNode.add(item);
            }
            jsonNodes.set("fundType", fundCompanyNode);
        }

        if (null != creatTimeLimit && creatTimeLimit > 0) {
            jsonNodes.put("creatTimeLimit", creatTimeLimit);
        }

        if (null != fundScale) {
            jsonNodes.put("fundScale", fundScale);
        }

        if (null != asc) {
            jsonNodes.put("asc", asc);
        }

        if (null != pageIndex) {
            jsonNodes.put("pageIndex", pageIndex);
        }

        if (null != pageSize) {
            jsonNodes.put("pageSize", pageSize);
        }

        try {
            return post("/v1/fund/rank", mapper.writeValueAsString(jsonNodes));
        } catch (JsonProcessingException e) {
            return ResultT.fail(e);
        }
    }

    @Override
    protected Request.Builder commonBuilder(String url, Map<String, String> headers) {
        return super.commonBuilder(URL + url, headers);
    }

    @Override
    protected ResultT dealResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            return ResultT.fail(response.code(), response.message());
        }

        JsonNode responseNode = mapper.readTree(response.body().string());
        if (200 == responseNode.get("code").asInt()) {
            return ResultT.success(responseNode.get("code").asInt(), mapper.writeValueAsString(responseNode.get("data")));
        } else {
            return ResultT.fail(responseNode.get("code").asInt(), responseNode.get("message").asText());
        }
    }

    public static void main(String[] args) throws Exception {
        /*
        create schema data_warehouse collate utf8_general_ci;

        create table fund_dimension
            (
                id            int auto_increment
                    primary key,
                code          varchar(20)  not null,
                name          varchar(255) null,
                type          varchar(255) null comment '类型',
                simple_pinyin varchar(50)  null comment '简拼',
                full_pinyin   varchar(255) null comment '全拼',
                constraint fund_dimension_code_uindex
                    unique (code)
            );

        create table fund_fact
            (
                id               int auto_increment
                    primary key,
                code             varchar(50)  not null,
                name             varchar(255) null,
                day              varchar(50)  null,
                unit_net_worth   double       null comment '单位净值',
                net_worth_growth double       null comment '净值涨幅'
            );
         */

        DoctorXiong dx = new DoctorXiong();
        ResultT result = dx.fundDetailList("2000-01-01", "2021-12-13", "000478", "160119", "090010", "004752", "001594", "001595", "519915", "501050", "519671", "501009", "160716", "000071", "012820");

        String data = (String) result.getData();

        List<List<Object>> values = new ArrayList<>();

        ArrayNode dataNodes = (ArrayNode) dx.mapper.readTree(data);
        for (JsonNode node : dataNodes) {
            if (!node.has("code") || !node.has("name") || !node.has("netWorthData")) {
                System.out.println(dx.mapper.writeValueAsString(node));
                continue;
            }
            String code = node.get("code").asText();
            String name = node.get("name").asText();
            ArrayNode netWorthData = (ArrayNode) node.get("netWorthData");
            for (JsonNode netWorthNode : netWorthData) {
                ArrayNode netWorthArrayNode = (ArrayNode) netWorthNode;

                List<Object> value = new ArrayList<>();
                value.add(code);
                value.add(name);
                value.add(netWorthArrayNode.get(0).asText());
                value.add(netWorthArrayNode.get(1).asDouble());
                value.add(netWorthArrayNode.get(2).asDouble());

                values.add(value);
            }
        }

        JdbcHelper jdbc = new JdbcHelper("jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8", "root", "123456");
        jdbc.batchUpdate("insert into fund_fact(code,name,day,unit_net_worth,net_worth_growth) values(?,?,?,?,?)", values, 100);
    }
}
