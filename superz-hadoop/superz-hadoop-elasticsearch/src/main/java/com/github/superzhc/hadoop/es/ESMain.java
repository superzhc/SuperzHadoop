package com.github.superzhc.hadoop.es;

import com.github.superzhc.hadoop.es.*;
import com.github.superzhc.hadoop.es.util.IndexUtils;

import com.github.superzhc.hadoop.es.search.ESIndexsSearch;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

/**
 * 2020年06月22日 superz add
 */
public class ESMain
{
//    static{
//        PropertiesUtils.read("./elasticsearch.properties");
//    }

    public static void main(String[] args) {
        try (ESClient client = ESClient.create("",1234/*PropertiesUtils.get("es.host"), PropertiesUtils.getInt("es.port")*/)) {
//            ESDocument document = new ESDocument(client,"002", "e0f2dc0c-3dcf-44ab-b0ca-45b64004eb79_002");
//            String str = document.get();
//            System.out.println(new JSONFormat().format(str));
//            ESSearch search = new ESIndexsSearch(client, "002", "003", "004", "005");
//            System.out.println(search.queryAll());

            // 获取所有索引
//            List<String> indices = IndexUtils.indices(client);
//            for (String index : indices) {
//                ESIndex esIndex = new ESIndex(client, index);
//                System.out.println(esIndex.mapping());
//            }

            SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());

            ESScroll scroll = new ESScroll(client);
            String ret = scroll.query(builder.toString(), "1m");
            System.out.println(ret);
            int count = 10;
            while (count > 0) {
                ret = scroll.get("1m");
                System.out.println(ret);
                count--;
            }
            ret = scroll.clear();
            System.out.println(ret);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
//
//        System.out.println(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).toString());
//
//        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
//        builder.from(0);
//        builder.size(10);
//        builder.fetchSource(new String[] {"infodate_date", "pubdate_date" }, null);
//        builder.sort("created_on", SortOrder.ASC);
//        builder.sort("name",SortOrder.DESC);
//        System.out.println(builder.toString());

//        // 时间范围
//        System.out.println(QueryBuilders.rangeQuery("dt").gte("2020-01-01").lte("2020-06-17"));
//
//        System.out.println(QueryBuilders.multiMatchQuery("aaa","xxx","yyyy"));
//
//        System.out.println(QueryBuilders.queryStringQuery("this AND that OR thus").defaultField("tttt"));
    }
}
