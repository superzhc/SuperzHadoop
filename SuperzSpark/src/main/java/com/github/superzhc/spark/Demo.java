package com.github.superzhc.spark;

//import com.github.superzhc.SparkDao;
//import com.github.superzhc.dataframe.SparkDataFrame;
//import org.apache.spark.sql.Row;

import java.util.List;

/**
 * 2020年06月08日 superz add
 */
public class Demo
{
    public static void main(String[] args) {
//        System.setProperty("livy.url", "http://192.168.186.52:18998");// 配置实际的地址
//        System.setProperty("livy.job.jars","D:\\superz\\FileParsing\\SparkLivyExecuteor\\target\\SparkLivyExecuteor-0.0.1.jar");// 配置LivySparkLab_Simple包的实际地址
//        //        String url = "jdbc:mysql://192.168.186.13:3306/superz?user=root&password=Gepoint&useSSL=false";
//        String url="";
//        Integer sessionId = null;
////        sessionId = 809;
//        SparkDao dao;
//        if (null == sessionId)
//            dao = new SparkDao(url);// 新建一个连接
//        else
//            dao = new SparkDao(url, sessionId);// 复用原有的连接
//        // 执行 mysql数据库
//        //        SparkDataFrame df = dao.query("select * from test1", "testx");
//        // 执行hive
//        SparkDataFrame df = dao.query("select * from superz_test where name='3333'", "testx");
//
//        System.out.println("数据的条数：" + df.count());
//        // 获取数据到本地
//        Row[] rows = df.take(100);
//        for (Row row : rows) {
//            System.out.println(row.toString());
//        }
//        SparkDataFrame df2 = df.execute("select id,name from testx");
//        List<Row> rows1 = df.takeAsList(100);
//        for (Row row : rows1) {
//            System.out.println(row.toString());
//        }
    }
}
