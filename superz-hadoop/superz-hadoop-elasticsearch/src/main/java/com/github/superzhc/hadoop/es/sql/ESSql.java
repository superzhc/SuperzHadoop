package com.github.superzhc.hadoop.es.sql;

import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.ESCommon;
import com.github.superzhc.hadoop.es.utils.ResponseUtils;
import org.elasticsearch.client.Response;

/**
 * @author superz
 * @create 2022/5/5 10:41
 **/
public class ESSql extends ESCommon {
    public ESSql(ESClient client) {
        super(client);
    }

    public String show(String sql) {
        Response response = client.post("/_sql?format=txt", "{\"query\":\"" + escape(sql) + "\"}");
        return ResponseUtils.getEntity(response);
    }

    public String sql(String sql) {
        // [POST /_xpack/sql] is deprecated! Use [POST /_sql] instead.
        // Response response = client.post("_xpack/sql?format=txt", "{\"query\":\"" + sql + "\"}");
        Response response = client.post(formatJson("/_sql"), "{\"query\":\"" + escape(sql) + "\"}");
        return ResponseUtils.getEntity(response);
    }

    public String translate(String sql) {
        Response response = client.post("/_sql/translate", "{\"query\":\"" + escape(sql) + "\"}");
        return ResponseUtils.getEntity(response);
    }

    private String escape(String sql) {
        return sql.replaceAll("\"", "\\\\\"");
    }
}
