package com.github.superzhc.data.others;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;
import com.github.superzhc.data.utils.UnitConversionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * AnyKnew
 * 网址：https://www.anyknew.com/
 *
 * @author superz
 * @create 2022/2/17 18:56
 */
public class AnyKnew extends HttpData {
    public ResultT yesterday() {
        String url = "https://www.anyknew.com/api/v1/rankings/yesterday";
        return get(url);
    }

    /**
     * 获取百度热点和百度贴吧热榜
     *
     * @return
     */
    public ResultT baidu() {
        return execute("baidu");
    }

    public ResultT weibo() {
        return execute("weibo");
    }

    public ResultT zhihu() {
        return execute("zhihu");
    }

    public ResultT sina() {
        return execute("sina");
    }

    public ResultT netease() {
        return execute("163");
    }

    public ResultT qihu() {
        return execute("360");
    }

    public ResultT toutiao() {
        return execute("toutiao");
    }

    public ResultT xueqiu() {
        return execute("xueqiu");
    }

    public ResultT investing() {
        return execute("investing");
    }

    public ResultT wallstreetcn() {
        return execute("wallstreetcn");
    }

    public ResultT eastmoney() {
        return execute("eastmoney");
    }

    public ResultT caixin() {
        return execute("caixin");
    }

    public ResultT guokr() {
        return execute("guokr");
    }

    public ResultT sanliukr() {
        return execute("36kr");
    }

    public ResultT yc() {
        return execute("yc");
    }

    public ResultT kepuchina() {
        return execute("kepuchina");
    }

    public ResultT cnbeta() {
        return execute("cnbeta");
    }

    public ResultT zol() {
        return execute("zol");
    }

    public ResultT smzdm() {
        return execute("smzdm");
    }

    public ResultT autohome() {
        return execute("autohome");
    }

    public ResultT jiemian() {
        return execute("jiemian");
    }

    public ResultT thepaper() {
        return execute("thepaper");
    }

    public ResultT pearvideo() {
        return execute("pearvideo");
    }

    public ResultT bilibili() {
        return execute("bilibili");
    }

    public ResultT mtime() {
        return execute("mtime");
    }

    public ResultT gamesky() {
        return execute("gamesky");
    }

    public ResultT endata() {
        return execute("endata");
    }

    /**
     * stream前100名实时在线人数
     *
     * @return
     */
    public ResultT steam() {
        return execute("steam");
    }

    public ResultT v2ex() {
        return execute("v2ex");
    }

    public ResultT toutiaoio() {
        return execute("toutiaoio");
    }

    public ResultT oschina() {
        return execute("oschina");
    }

    private ResultT execute(String type) {
        String url = String.format("https://www.anyknew.com/api/v1/sites/%s", type);
        return get(url);
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        ObjectMapper mapper = new ObjectMapper();
        AnyKnew ak = new AnyKnew();

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            String sql = "INSERT INTO any_knew_hot_news(id, title, more, tag, sub_tag, create_time, update_time) VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE id = ?, title = ?, more = ?, tag = ?, sub_tag = ?, update_time =?";

            String[] types = {"baidu", "weibo", "zhihu", "sina", "163", "360", "toutiao", "xueqiu", "investing", "wallstreetcn"
                    , "eastmoney", "caixin", "guokr", "36kr", "yc", "kepuchina", "cnbeta", "zol", "smzdm", "autohome", "jiemian"
                    , "thepaper", "pearvideo", "bilibili", "mtime", "gamesky", "endata", "v2ex", "toutiaoio", "oschina"};

            while (true) {
                System.out.println("---------------------------------------------------------------------------");
                System.out.println("                             获取热榜数据                                   ");
                System.out.println("---------------------------------------------------------------------------");
                for (String type : types) {
                    String data = (String) ak.execute(type).getData();
                    JsonNode dataNode = mapper.readTree(data);
                    JsonNode site = dataNode.get("data").get("site");
                    String tag = site.get("attrs").get("cn").asText();
                    for (JsonNode sub : site.get("subs")) {
                        List<List<Object>> params = new ArrayList<>();

                        String subTag = sub.get("attrs").get("cn").asText();
                        for (JsonNode item : sub.get("items")) {
                            String iid = item.get("iid").asText();
                            String title = item.get("title").asText();
                            String more = null == item.get("more") ? null : item.get("more").asText();
                            Long addDate = item.get("add_date").asLong();
                            Long currentDate = System.currentTimeMillis();

                            List<Object> param = new ArrayList<>();
                            param.add(iid);
                            param.add(title);
                            param.add(UnitConversionUtils.number(more));
                            param.add(tag);
                            param.add(subTag);
                            param.add(new Timestamp(addDate * 1000));
                            param.add(new Timestamp(currentDate));
                            param.add(iid);
                            param.add(title);
                            param.add(UnitConversionUtils.number(more));
                            param.add(tag);
                            param.add(subTag);
                            param.add(new Timestamp(currentDate));
                            params.add(param);
                        }
                        jdbc.batchUpdate(sql, params);
                    }
                }

                // 每10分钟更新一次热榜数据
                Thread.sleep(1000 * 60 * 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
