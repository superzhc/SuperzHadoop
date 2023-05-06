package com.github.superzhc.hadoop.es;

import com.github.superzhc.hadoop.es.search.ESNewSearch;
import com.github.superzhc.hadoop.es.sql.ESSql;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortBuilders;
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
        String dsl = "{\"from\":0,\"query\":{\"bool\":{\"must\":[{\"term\":{\"labels.job\":\"minifi\"}},{\"term\":{\"name\":\"minifi_config_yml_file_checksum\"}}],\"filter\":[{\"range\":{\"@timestamp\":{\"from\":\"2023-05-05T13:47:50.497Z\",\"to\":null,\"include_lower\":false,\"include_upper\":true}}}]}},\"sort\":[{\"@timestamp\":{\"order\":\"desc\"}}],\"track_total_hits\":2147483647,\"collapse\":{\"field\":\"labels.instance\"}}";
        String result = searchClient.queryDSL(dsl, indexPattern);
        System.out.println(result);
    }

    @Test
    public void testComplexQuery() {
        String indexPattern = "prometheusbeat-7.3.1-*";

        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 遍历所有文档获取匹配结果
        builder.trackTotalHits(true);

        // builder.fetchField("@timestamp").fetchField("labels.instance").fetchField("labels.remote_pull_url").fetchField("value");

        // 查询条件
        String id = "efe15707-0187-1000-9f65-70aef98433a9";
        String fileName = "10.90.15.222:18089/nifi-registry-api/minifi/config/pull/efe15707-0187-1000-9f65-70aef98433a9.yml";
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("labels.job", "minifi"))
                .must(QueryBuilders.termQuery("name", "minifi_config_yml_file_checksum"))
                // 因保存的字段是keyword，匹配查询是必须相等的逻辑
                // .must(QueryBuilders.matchQuery("labels.remote_pull_url",fileName))
                .must(QueryBuilders.wildcardQuery("labels.remote_pull_url", String.format("*%s*",id)))
                .filter(QueryBuilders.rangeQuery("@timestamp").gt(LocalDateTime.now().minusDays(1)));
        builder.query(queryBuilder);

        // 排序
        builder.sort("@timestamp", SortOrder.DESC);

        // 去重，排序好像可以实现最新一条数据展示，待多服务器后验证
        CollapseBuilder collapseBuilder = new CollapseBuilder("labels.instance")
                //.setInnerHits(new InnerHitBuilder("most_recent").setSize(1).addSort(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC)))
                ;
        builder.collapse(collapseBuilder);

        // AggregationBuilder aggregationBuilder= AggregationBuilders

        builder.from(0);
        builder.size(1000);// TODO 查询出所有结果

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
        String sql = "SELECT * FROM \"prometheusbeat-7.3.1-*\" WHERE \"labels.job\"='minifi' AND name='minifi_config_yml_file_checksum' ORDER BY \"@timestamp\" DESC Limit 100";
        // ES 返回报错信息：SELECT DISTINCT is not yet supported
        // sql = "SELECT DISTINCT labels.instance FROM \"prometheusbeat-7.3.1-*\" WHERE \"labels.job\"='minifi' AND name='minifi_config_yml_file_checksum' ORDER BY \"@timestamp\" DESC";
        // String sql="SELECT * FROM \"my_test\"";
        String result = sqlClient.show(sql);
        System.out.println(result);
    }
}
