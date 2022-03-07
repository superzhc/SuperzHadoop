package com.github.superzhc.data.others;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.common.HtmlData;
import org.seimicrawler.xpath.JXNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AK 热点归档
 *
 * @author superz
 * @create 2022/2/18 1:52
 */
public class AnyKnewHotPointArchive extends HtmlData {
    public List<List<Object>> day(String d) {
        // 示例：https://www.anyknew.com/rankings/day/20181212
        String url = String.format("https://www.anyknew.com/rankings/day/%s", d);
        Doc doc = get(url);

        List<List<Object>> params = new ArrayList<>();

        // 获取内容体
        JXNode node = doc.selOne("//div[@class=\"list-box\"]");
        //热点
        JXNode hotNode = node.selOne("//div[@class=\"list-topics\"][1]");
        List<JXNode> hotTopics = hotNode.sel("//div[@class=\"topic\"]");
        if (null != hotTopics) {
            for (JXNode hotTopic : hotTopics) {
                String rank = hotTopic.selOne("//div[contains(@class,'topic-rank')]/text()").asString();
                String content = hotTopic.selOne("//div[@class=\"topic-content\"]/allText()").asString();

                List<Object> param = new ArrayList<>();
                param.add(d);
                param.add(rank);
                param.add(content);
                param.add("热点");
                params.add(param);
            }
        }

        //讨论
        JXNode discussNode = node.selOne("//div[@class=\"list-topics\"][2]");
        List<JXNode> discussTopics = discussNode.sel("//div[@class=\"topic\"]");
        if (null != discussTopics) {
            for (JXNode discussTopic : discussTopics) {
                String rank = discussTopic.selOne("//div[contains(@class,'topic-rank')]/text()").asString();
                String content = discussTopic.selOne("//div[@class=\"topic-content\"]//a/text()").asString();

                List<Object> param = new ArrayList<>();
                param.add(d);
                param.add(rank);
                param.add(content);
                param.add("讨论");
                params.add(param);
            }
        }
        return params;
    }

    public static void main(String[] args) {
        AnyKnewHotPointArchive akhp = new AnyKnewHotPointArchive();

        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            LocalDate start = LocalDate.of(2022, 2, 19);
            LocalDate end = LocalDate.now();
            for (; end.isAfter(start); start = start.plusDays(1L)) {
                List<List<Object>> params = akhp.day(start.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                jdbc.batchUpdate("INSERT INTO any_knew_hot_point_archive(riqi,paihang,title,tag) values(?, ?, ?, ?)", params);
            }
        }
    }
}
