package com.github.superzhc.financial.data.news;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/18 15:00
 **/
public class JieMian {
    public static Table news() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.news.JieMian.news();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = news();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
