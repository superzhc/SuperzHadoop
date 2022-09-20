package com.github.superzhc.data.news;

import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/20 17:02
 **/
public class BarronsChina {
    public static void main(String[] args) {
        String url="http://www.barronschina.com.cn/index/shortNews";

        String result= HttpRequest.get(url).body();

        Document doc= Jsoup.parse(result);

        List<Map<String,String>> dataRows=new ArrayList<>();

        Elements elements=doc.select("dd");
        for (Element element:elements){
            Map<String,String> dataRow=new LinkedHashMap<>();
            dataRow.put("title",element.selectFirst("strong").text());
//            dataRow.put("date",element)
            dataRow.put("brief",element.selectFirst(".short").html());

            dataRows.add(dataRow);
        }

        System.out.println(dataRows);
    }
}
