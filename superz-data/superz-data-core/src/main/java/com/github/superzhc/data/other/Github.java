package com.github.superzhc.data.other;

import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/12 14:25
 **/
public class Github {
    public static void main(String[] args) {
        String query="hudi";
        String url="https://github.com/search";

        Map<String,Object> params=new HashMap<>();
        params.put("q", query);
        params.put("s","");
        params.put("o","desc");
        params.put("type","Repositories");

        String html=HttpRequest.get(url,params).body();
        Document doc= Jsoup.parse(html);

        Elements elements=doc.select(".repo-list li");
        for (Element element:elements){
            /*
            const a = $(this).find('.f4.text-normal > a');
            const single = {
                title: a.text(),
                author: a.text().split('/')[0].trim(),
                link: host.concat(a.attr('href')),
                description: $(this).find('div p').text().trim(),
            };
            return single;
            */
        }
    }
}
