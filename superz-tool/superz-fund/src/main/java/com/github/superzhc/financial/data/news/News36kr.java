package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/15 0:35
 */
public class News36kr {
    private static final Logger log = LoggerFactory.getLogger(News36kr.class);

    private static final String pattern = "\"itemList\":(\\[.*?\\])";

    /**
     * 最新资讯
     *
     * @return
     */
    public static Table latest() {
        return execute("web_news");
    }

    public static Table recommend() {
        return execute("web_recommend");
    }

    /**
     * 创投
     *
     * @return
     */
    public static Table vc() {
        return execute("contact");
    }

    public static Table car() {
        return execute("travel");
    }

    public static Table technology() {
        return execute("technology");
    }

    /**
     * 企服
     *
     * @return
     */
    public static Table enterpriseService() {
        return execute("enterpriseservice");
    }

    /**
     * 创新
     *
     * @return
     */
    public static Table innovate() {
        return execute("innovate");
    }

    /**
     * 房产
     *
     * @return
     */
    public static Table realEstate() {
        return execute("real_estate");
    }

    public static Table life() {
        return execute("happy_life");
    }

    public static Table other() {
        return execute("other");
    }

    /**
     * 财经
     *
     * @return
     */
    public static Table finance() {
        return execute("ccs");
    }

    private static Table execute(String category) {
        String url = "https://www.36kr.com/information/ccs";

        String result = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(result);

        String data = null;
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(document.data());
        if (matcher.find()) {
            data = matcher.group(1);
            log.debug(data);
        }

        if (null == data) {
            return null;
        }

        JsonNode json = JsonUtils.json(data);
        List<String[]> tableData = JsonUtils.objectArray(json, "templateMaterial");
        Table table = TableUtils.build(tableData);
        return table;
    }

    public static void main(String[] args) {
        Table table = other();

        if (null == table) {
            System.out.println("数据为空");
            return;
        }

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
