package com.github.superzhc.hadoop.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.hadoop.es.index.ESIndex;
import com.github.superzhc.hadoop.es.search.ESIndexsSearch;
import com.github.superzhc.hadoop.es.search.ESNewSearch;
import com.github.superzhc.hadoop.es.search.ESSearch;
import com.github.superzhc.hadoop.es.sys.ESCat;
import com.github.superzhc.hadoop.es.sys.ESCluster;

/**
 * 2020年06月22日 superz add
 */
public class ESMain {
    public static void main(String[] args) {
        String path = "D:\\downloads\\tg\\";
        String fileName = "xxx.txt";
        path += fileName;

        //FileData fileData = FileData.txt(path, "GB2312");
        //fileData.preview();

        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        ObjectMapper mapper = new ObjectMapper();


        try (ESClient client = ESClient.create("localhost", 9200)) {
            String index = "shegong";
            String result;

//            ESCat esCat=new ESCat(client);
//            System.out.println(esCat.count());
//            System.out.println(esCat.allocation());
//            System.out.println(esCat.fielddata());
//            System.out.println(esCat.indices());
//            System.out.println(esCat.shards());

//            ESCluster esCluster = new ESCluster(client);
//            System.out.println(esCluster.info());
//            System.out.println(esCluster.health());
//            System.out.println(esCluster.settings());
//            System.out.println(esCluster.stats());


            ESIndex esIndex = new ESIndex(client);
//            // result = esIndex.create(index, 10, 1);
//            //System.out.println(result);
//            result=esIndex.mapping(index);
//            System.out.println(result);

//            ESDocument esDocument = new ESDocument(client);

//            try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
//                Long count = jdbc.aggregate("select count(*) from xxx");
//                int batchSize = 10000;
//
//                Long size = count / batchSize;
//                long remain = count % batchSize;
//                for (long i = 4676168 / batchSize, len = (remain == 0 ? size : size + 1); i < len; i++) {
//                    List<Map<String, Object>> datas = jdbc.query("select * from xxx limit " + (i * size) + "," + batchSize);
//                    for (Map<String, Object> data : datas) {
//                        String mobile = (String) data.get("mobile");
//                        String tel = (String) data.get("tel");
//                        mobile = mobile.replace("/", "");
//                        tel = tel.replace("/", "");
//                        String id = null == mobile || mobile.trim().length() == 0 ? (null == tel || tel.trim().length() == 0 ? UUID.randomUUID().toString() : tel) : mobile;
//                        esDocument.upsert(index, id, mapper.writeValueAsString(data));
//                    }
//                }
//            }

            //            FileData.FileReadSetting setting = new FileData.FileReadSetting(10000, new Function<List<Object>, Boolean>() {
//                @Override
//                public Boolean apply(List<Object> objects) {
//                    for (Object object : objects) {
//                        String line = (String) object;
//                        String[] ss = line.split("----");
//
//                        String id, info;
//                        if (ss.length == 2) {
//                            id = null == ss[0] || ss[0].trim().length() == 0 ? UUID.randomUUID().toString() : ss[0];
//                            info = ss[1];
//                        } else {
//                            id = UUID.randomUUID().toString();
//                            info = line;
//                        }
//
//                        if (null != info) {
//                            info = info.replace("\"", "'");
//                        }
//
//                        esDocument.upsert(index, id, "{\"kuaishou\":\"" + info + "\"}");
//                    }
//                    return true;
//                }
//            });
//            fileData.read(setting);

            ESNewSearch esSearch = new ESNewSearch(client);
//            result = esSearch.queryDSL("{\n" +
//                    "    \"from\":1000,\n" +
//                    "    \"size\":100,\n" +
//                    "    \"query\":{\n" +
//                    "        \"match\":{\n" +
//                    //"            \"name\":\"\"\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}", index);
            result=esSearch.queryAll(index);
            System.out.println(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
