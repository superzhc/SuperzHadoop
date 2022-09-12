package com.github.superzhc.financial.data.other;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jdbc.JdbcHelper;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlReadOptions;
import tech.tablesaw.io.json.JsonReadOptions;
import tech.tablesaw.io.xlsx.XlsxReadOptions;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 经济政策不确定性指数
 * http://www.policyuncertainty.com/index.html
 *
 * @author superz
 * @create 2022/2/24 10:22
 */
public class EPUIndex {
    /**
     * 经济政策不确定性指数
     * http://www.policyuncertainty.com/index.html
     *
     * @param index 指定的国家名称, e.g. “China”
     */
    public Table articleEPUIndex(String index) {
        Table table = null;
        try {
            switch (index) {
                case "China":
                case "China New":
                    index = "China";
                    break;
                case "USA":
                    index = "US";
                    break;
                case "Hong Kong":
                    index = "HK";
                    table = Table.read().url(String.format("http://www.policyuncertainty.com/media/%s_EPU_Data_Annotated.xlsx", index));
                    return table;
                case "Germany":
                case "France":
                case "Italy":
                    index = "Europe";
                    break;
                case "South Korea":
                    index = "Korea";
                    break;
                case "Spain New":
                    index = "Spain";
                    break;
                case "Ireland":
                case "Chile":
                case "Colombia":
                case "Netherlands":
                case "Singapore":
                case "Sweden":
                    XlsxReadOptions options = XlsxReadOptions
                            .builderFromUrl(String.format("http://www.policyuncertainty.com/media/%s_Policy_Uncertainty_Data.xlsx", index))
                            .build();
                    table = Table.read().usingOptions(options);
                    return table;
                case "Greece":
                    table = Table.read().url(String.format("http://www.policyuncertainty.com/media/FKT_%s_Policy_Uncertainty_Data.xlsx", index));
                    return table;
            }
            table = Table.read().csv(new URL(String.format("http://www.policyuncertainty.com/media/%s_Policy_Uncertainty_Data.csv", index)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        final ObjectMapper mapper = new ObjectMapper();

//        EPUIndex epuIndex = new EPUIndex();
//        String[] indeics = {/*"China"*//*, "Hong Kong", "Germany"*/"Singapore"};
//        for (String index : indeics) {
//            Table table = epuIndex.articleEPUIndex(index);
//            System.out.println(table.structure());
//            System.out.println(table.shape());
//            //System.out.println(table);
//        }

//        // 报错，每行的列数不同会报错
//        HtmlReadOptions options = HtmlReadOptions
//                .builderFromUrl("http://mba.tuck.dartmouth.edu/pages/faculty/ken.french/data_library.html")
//                .header(false)
//                .tableIndex(0)
//                .build();
//
//        Table table = Table.read().usingOptions(options);
//        System.out.println(table.print());

//        Map<String, String> headers = new HashMap<>();
//        headers.put("Accept", "*/*");
//        headers.put("Accept-Encoding", "gzip, deflate");
//        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
//        headers.put("Cache-Control", "no-cache");
//        headers.put("Connection", "keep-alive");
//        headers.put("Host", "searchapi.eastmoney.com");
//        headers.put("Pragma", "no-cache");
//        headers.put("Referer", "http://so.eastmoney.com/");
//        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
//
//        String stock = "601628";
//        Map<String, String> params = new HashMap<>();
//        params.put("type", "8196");
//        params.put("pageindex", "1");
//        params.put("pagesize", "20");
//        params.put("keyword", "(" + stock + ")()");
//        params.put("name", "zixun");
//        params.put("_", "1608800267874");
//        String ret = HttpRequest.get("http://searchapi.eastmoney.com//bussiness/Web/GetCMSSearchList", params, true)
//                .headers(headers)
//                .body();
//        System.out.println(ret);

        String AKShareUrl = "http://127.0.0.1:18080/api/%s";
        String ret = HttpRequest.get(String.format(AKShareUrl, "sport_olympic_hist"))
                .body();

        JsonNode datas = mapper.readTree(ret);
        // 示例数据：{"id":133693,"name":"Constantin Zahei","sex":"M","age":32.0,"height":null,"weight":null,"team":"Romania","noc":"ROU","games":"1936 Summer","year":1936,"season":"Summer","city":"Berlin","sport":"Equestrianism","event":"Equestrianism Men's Three-Day Event, Individual","medal":null}
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            Object[][] params = new Object[datas.size()][];
            int cursor = 0;
            for (JsonNode data : datas) {
                Object[] param = new Object[]{
                        /*data.get("id").asInt()*/cursor + 1,
                        null == data.get("name") ? null : data.get("name").asText(),
                        null == data.get("sex") ? null : data.get("sex").asText(),
                        null == data.get("age") ? null : data.get("age").asDouble(),
                        null == data.get("height") ? null : data.get("height").asDouble(),
                        null == data.get("weight") ? null : data.get("weight").asDouble(),
                        null == data.get("team") ? null : data.get("team").asText(),
                        null == data.get("noc") ? null : data.get("noc").asText(),
                        null == data.get("games") ? null : data.get("games").asText(),
                        null == data.get("year") ? null : data.get("year").asInt(),
                        null == data.get("season") ? null : data.get("season").asText(),
                        null == data.get("city") ? null : data.get("city").asText(),
                        null == data.get("sport") ? null : data.get("sport").asText(),
                        null == data.get("event") ? null : data.get("event").asText(),
                        null == data.get("medal") ? null : data.get("medal").asText()
                };
                params[cursor++] = param;
            }
            jdbc.batchUpdate("insert into sport_olympic_hist values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", params, 1000);
        }
    }
}
