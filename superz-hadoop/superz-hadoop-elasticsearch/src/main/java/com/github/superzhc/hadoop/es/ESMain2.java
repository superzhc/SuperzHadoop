package com.github.superzhc.hadoop.es;

import com.github.superzhc.common.utils.JsonFormatUtils;
import com.github.superzhc.hadoop.es.index.ESIndex;

import java.io.IOException;

/**
 * @author superz
 * @create 2022/4/26 15:27
 **/
public class ESMain2 {
    public static void main(String[] args) {
        try (ESClient esClient =
                     ESClient.create("http", "10.90.15.142", 9200, "elastic", "xgxx@elastic")
//                     ESClient.create("10.90.15.142", 9200, "superz", "superz")
//                     new ESClient(new HttpHost[]{new HttpHost("10.90.15.142", 9200, "https")}, "D:\\xgitbi\\20220428\\elastic-certificates.p12")
        ) {
//            ESCat cat=new ESCat(esClient);
//            System.out.println(cat.health());

            // 修改密码
//            String[] sysUsers = {"elastic", "apm_system", "kibana_system", "logstash_system", "beats_system", "remote_monitoring_user", "metricbeat_system"};
//            for (String sysUser : sysUsers) {
//                String path = String.format("/_security/user/%s/_password", sysUser);//"/_xpack/security/user/elastic/_password";
//                String json = String.format("{\"password\":\"xgxx@%s\"}", sysUser);
//                String result = ResponseUtils.getEntity(esClient.put(path, json));
//                System.out.println(result);
//            }
//            System.out.println(esClient.ping());

            // String result=new ESIndex(esClient).indices();
            // System.out.println(JSONUtils.format(result));

            String index = "service_instance_sla-20220423";

//            String sql = "select * from \\\"service_instance_sla-20220423\\\" where time_bucket>202204231414 order by time_bucket asc limit 5";
//            ESSql esSql = new ESSql(esClient);
//            System.out.println(JSONUtils.format(esSql.translate(sql)));
//            System.out.println(JSONUtils.format(esSql.sql(sql)));

            index = "superz_test1";

            ESIndex esIndex = new ESIndex(esClient);
            //String result=esIndex.create(index,3,2);
            String result = esIndex.settings(index);
            System.out.println(JsonFormatUtils.format(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
