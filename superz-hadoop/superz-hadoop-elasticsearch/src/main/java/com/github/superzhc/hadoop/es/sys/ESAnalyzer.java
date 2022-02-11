package com.github.superzhc.hadoop.es.sys;

import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.ESCommon;
import com.github.superzhc.hadoop.es.util.ResponseUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public class ESAnalyzer extends ESCommon
{
    public ESAnalyzer(ESClient client){
        super(client);
    }

    /**
     * 测试分词器
     * standard analyzer：set, the, shape, to, semi, transparent, by, calling, set_trans, 5（默认的是standard）
     * simple analyzer：set, the, shape, to, semi, transparent, by, calling, set, trans
     * whitespace analyzer：Set, the, shape, to, semi-transparent, by, calling, set_trans(5)
     * language analyzer（特定的语言的分词器，比如说，english，英语分词器）：set, shape, semi, transpar, call, set_tran, 5
     * @return
     */
    public String Analyzer(String analyzer, String text) {
        String url = "/_analyze";
        String query = "{\"analyzer\": \"" + analyzer + "\",\"text\": \"" + text + "\"}";
        return ResponseUtils.getEntity(client.get(url, query));
    }
}
