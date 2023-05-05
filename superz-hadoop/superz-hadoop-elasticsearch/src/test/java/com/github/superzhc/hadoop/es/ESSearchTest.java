package com.github.superzhc.hadoop.es;

import com.github.superzhc.hadoop.es.search.ESNewSearch;
import com.github.superzhc.hadoop.es.sql.ESSql;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author superz
 * @create 2023/4/12 11:28
 **/
public class ESSearchTest extends ESClientTest {

    ESNewSearch searchClient;
    ESSql sqlClient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        searchClient = new ESNewSearch(client);
        sqlClient = new ESSql(client);
    }

    @Test
    public void testQueryAll() {
        String result = searchClient.queryAll();
        System.out.println(result);
    }

    @Test
    public void testQueryString() {
        String indexPattern = "prometheusbeat-7.3.1-*";

        String q = "labels.job:minifi";

        String result = searchClient.queryString(q, indexPattern);
        System.out.println(result);
    }

    @Test
    public void testQueryDSL() {
        String indexPattern = "prometheusbeat-7.3.1-*";

        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 查询条件
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("labels.job", "minifi"))
                .must(QueryBuilders.termQuery("name", "minifi_config_yml_file_checksum"));
        builder.query(queryBuilder);

        // 排序
        builder.sort("@timestamp", SortOrder.DESC);

        // 去重


        String query = builder.toString();
        String result = searchClient.queryDSL(query, indexPattern);
        System.out.println(result);
    }

    public void testRange() {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gt(100);

        builder.query(queryBuilder);

        String query = builder.toString();
        String result = searchClient.queryDSL(query);
        System.out.println(result);
    }

    @Test
    public void testSQL() {
        //String sql = "SELECT * FROM \"prometheusbeat-7.3.1-*\" WHERE \"labels.job\"='minifi' AND name='minifi_config_yml_file_checksum' ORDER BY \"@timestamp\" DESC";
        String sql="SELECT * FROM \"my_test\"";
        String result = sqlClient.show(sql);
        System.out.println(result);
    }
}
