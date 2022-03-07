package com.github.superzhc.data.others;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鱼塘热榜
 * 网址：https://mo.fish/
 *
 * @author superz
 * @create 2022/2/20 2:24
 */
public class MoFish extends HttpData {
    private static final String API_URL = "https://api.tophub.fun/v2/GetAllInfoGzip?id=%s&page=%d&type=pc";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 虎扑热榜
     *
     * @param page
     * @return
     */
    public ResultT hupu(Integer page) {
        return execute("2", page);
    }

    /**
     * 知乎热榜
     *
     * @param page
     * @return
     */
    public ResultT zhihu(Integer page) {
        return execute("1", page);
    }

    public ResultT acFun(Integer page) {
        return execute("142", page);
    }

    public ResultT smzdm(Integer page) {
        return execute("117", page);
    }

    /**
     * 淘宝零食
     *
     * @param page
     * @return
     */
    public ResultT taobao_linshi(Integer page) {
        return execute("1050", page);
    }

    private ResultT execute(String id, Integer page) {
        String url = String.format(API_URL, id, page);
        ResultT r = get(url);

        try {
            String data = r.data();
            JsonNode node = mapper.readTree(data);
            Integer code = node.get("Code").asInt();
            String msg = node.get("Message").asText();
            String d = mapper.writeValueAsString(node.get("Data"));
            return ResultT.create(code, msg, d);
        } catch (Exception e) {
            r.setCode(1);
            return r;
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";
        String sql = "INSERT INTO mo_fish(id,title,create_time,type_name,url,approval_num,comment_num,hot_desc,description,img_url,is_rss,is_agree,source_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)" +
                " ON DUPLICATE KEY UPDATE id=?,title=?,create_time=?,type_name=?,url=?,approval_num=?,comment_num=?,hot_desc=?,description=?,img_url=?,is_rss=?,is_agree=?,source_name=?";

        StringBuilder finshFetchSource = new StringBuilder();
//        finshFetchSource.append(",");
//        finshFetchSource.append(
//                "110,111,112,114,115,116,117,118,119,10,11,12,1110,120,1107,121,1,122,2,123,1105,124,3,1104,125,1103,1102,126,6,127,1101,7,1100,128,8,129,9,1109,1108,1121,1120,130,131,132,1118,1117,133,134,1116,135,1115,136,1114,1113,138,1066,1065,1064,1063,1062,1061,1060,1059,1058,1057,90,1056,1077,1076,1075,1074,1073,1072,1071,1070,1069,1068,1067,2719,1080,1088,1087,1086,1085,1083,1082,1081,2724,2723,2722,2721,2720,1079,1112,139,1078,1111,2728,2727,2726,2725,1119,1090,1011,1099,1132,1010,1098,1131,1097,1130,1096,141,142,143,1008,1129,144,1007,1128,1127,146,1126,147"
//        );
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            MoFish moFish = new MoFish();

            // 获取所有榜
            Map<String, String> idAndNames = new HashMap<>();
            String moFishAllTypeUrl = "https://api.tophub.fun/GetAllType";
            String allTypeR = moFish.get(moFishAllTypeUrl).data();
            JsonNode allTypeRNode = mapper.readTree(allTypeR);
            JsonNode types = allTypeRNode.get("Data").get("全部");
            for (JsonNode type : types) {
                idAndNames.put(type.get("id").asText(), type.get("name").asText());
            }

            Thread.sleep(1000 * 3);

            while (true) {
                try {
                    for (Map.Entry<String, String> idAndName : idAndNames.entrySet()) {
                        String finshFetchSourceStr = finshFetchSource.toString() + ",";
                        if (finshFetchSourceStr.contains("," + idAndName.getKey() + ",")) {
                            continue;
                        }

                        // 优化此处逻辑
//                // 第一次需要获取分页
//                ResultT r = moFish.get(String.format(API_URL, idAndName.getKey(), 0));
//                String firstData = r.data();
//                JsonNode firstNode = mapper.readTree(firstData);
//                Integer page = firstNode.get("Data").get("page").asInt();
//                System.out.println("读取源[" + idAndName.getValue() + "](id=" + idAndName.getKey() + ")：共 " + page + " 页");

                        // 既然能出现的数据源，肯定是有一页的，初始化在第一次请求后
                        boolean first = true;
                        int page = 1;

                        for (int i = 0; i < page; i++) {
                            String data = moFish.execute(idAndName.getKey(), i).data();
                            JsonNode node = mapper.readTree(data);

                            if (first) {
                                page = node.get("page").asInt();
                                first = false;
                                System.out.println("读取源[" + idAndName.getValue() + "](id=" + idAndName.getKey() + ")：共 " + page + " 页");
                            }

                            List<List<Object>> params = new ArrayList<>();
                            for (JsonNode item : node.get("data")) {
                                List<Object> param = new ArrayList<>();

                                if (null == item.get("id")) {
                                    continue;
                                }

                                param.add(item.get("id").asInt());
                                param.add(null == item.get("Title") ? null : item.get("Title").asText());
                                param.add(null == item.get("CreateTime") ? System.currentTimeMillis() : new Timestamp(item.get("CreateTime").asLong() * 1000));
                                param.add(null == item.get("TypeName") ? null : item.get("TypeName").asText());
                                param.add(null == item.get("Url") ? null : item.get("Url").asText());
                                param.add(null == item.get("approvalNum") ? null : item.get("approvalNum").asInt());
                                param.add(null == item.get("commentNum") ? null : item.get("commentNum").asInt());
                                param.add(null == item.get("hotDesc") ? null : item.get("hotDesc").asText());
                                param.add(null == item.get("Desc") ? null : item.get("Desc").asText());
                                param.add(null == item.get("imgUrl") ? null : item.get("imgUrl").asText());
                                param.add(null == item.get("isRss") ? null : item.get("isRss").asText());
                                param.add(null == item.get("is_agree") ? null : item.get("is_agree").asInt());
                                param.add(idAndName.getValue());
                                // upsert 需要重复设置一遍数据
                                param.add(item.get("id").asInt());
                                param.add(null == item.get("Title") ? null : item.get("Title").asText());
                                param.add(null == item.get("CreateTime") ? System.currentTimeMillis() : new Timestamp(item.get("CreateTime").asLong() * 1000));
                                param.add(null == item.get("TypeName") ? null : item.get("TypeName").asText());
                                param.add(null == item.get("Url") ? null : item.get("Url").asText());
                                param.add(null == item.get("approvalNum") ? null : item.get("approvalNum").asInt());
                                param.add(null == item.get("commentNum") ? null : item.get("commentNum").asInt());
                                param.add(null == item.get("hotDesc") ? null : item.get("hotDesc").asText());
                                param.add(null == item.get("Desc") ? null : item.get("Desc").asText());
                                param.add(null == item.get("imgUrl") ? null : item.get("imgUrl").asText());
                                param.add(null == item.get("isRss") ? null : item.get("isRss").asText());
                                param.add(null == item.get("is_agree") ? null : item.get("is_agree").asInt());
                                param.add(idAndName.getValue());
                                params.add(param);
                            }
                            jdbc.batchUpdate(sql, params);

                            Thread.sleep(1000 * 3);
                        }

                        finshFetchSource.append(",").append(idAndName.getKey());
                    }

                    // 所有的都查询完成后，直接跳出捕获
                    break;
                } catch (Exception ex) {
                    //一直访问会出现无法获取数据的情况，因此休眠3分钟
                    Thread.sleep(1000 * 60 * 3);
                } finally {
                    System.out.println("已完成的数据源：");
                    System.out.println(finshFetchSource.substring(1));
                }
            }
        }
    }
}
