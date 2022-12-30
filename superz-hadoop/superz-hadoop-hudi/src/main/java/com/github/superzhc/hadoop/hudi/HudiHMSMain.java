package com.github.superzhc.hadoop.hudi;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;

import java.util.List;

import static com.github.superzhc.hadoop.hudi.data.AbstractData.DEFAULT_HIVE_METASTORE_URIS;

/**
 * @author superz
 * @create 2022/12/15 17:56
 **/
public class HudiHMSMain {
    public static void main(String[] args) throws Exception {
        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", DEFAULT_HIVE_METASTORE_URIS);

        // 直接通过HMS操作Hive Metadata
        IMetaStoreClient client = Hive.get(conf).getMSC();

        /*获取所有catalogs*/
//        client.getCatalogs().forEach(System.out::println);
        /*获取catalog为hive的描述信息*/
//        System.out.println(client.getCatalog("hive"));

        /*取catalog为hive的所有database*/
//        client.getAllDatabases("hive").forEach(System.out::println);
        /*获取catalog为hive，database为default的描述信息*/
//        System.out.println(client.getDatabase("hive","default"));

        /*获取catalog为hive，database名为default下的所有表*/
//        client.getTables("hive","default","*").forEach(System.out::println);

        /*获取指定表信息*/
        Table table = client.getTable("default", "superz_java_client_20221213150742");
//        System.out.println(table);
        /*获取表中列*/
        List<FieldSchema> fields=table.getSd().getCols();
//        fields.forEach(System.out::println);
        /*创建列*/
//        FieldSchema newField=new FieldSchema("col2","string","新增列2");
//        table.getSd().addToCols(newField);
        /*修改列，
        对于已有数据的列，修改列名会造成数据没了，bug？
        若将列名修改回原来，数据会正常展示
        */
//        FieldSchema modifyField=fields.get(7);
//        modifyField.setName("url");
//        modifyField.setComment("修改列");
        /**
         * 删除列
         * 1. 对无数据的列进行删除，成功
         * 2. 对有数据的列进行删除，报错
         */
//        fields.remove(9);
//        table.getSd().setCols(fields);

//        client.alter_table("default","superz_java_client_20221213150742",table);

        /*删除表*/
        client.dropTable("default","superz_spark_client_20221230144601");
    }
}
