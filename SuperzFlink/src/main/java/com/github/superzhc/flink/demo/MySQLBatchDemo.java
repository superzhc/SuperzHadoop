package com.github.superzhc.flink.demo;

import com.github.superzhc.db.JdbcHelper;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author superz
 * @create 2021/3/31 17:58
 */
public class MySQLBatchDemo {
    private static String driverClass = "com.mysql.cj.jdbc.Driver";
    private static String dbUrl =
            "jdbc:mysql://10.50.60.9:3306/superz_hadoop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private static String userName = "root";
    private static String passWord = "123456";

    public static void main(String[] args) {
        JdbcHelper jdbcHelper=new JdbcHelper(driverClass,dbUrl,userName,passWord);
        String sql="insert into t_statistics_bayonetpass(device_id,start_time,end_time,create_time,num) values(?,?,?,?,?)";
        List<Map<Integer,Object>> lst=new ArrayList<>();
        for(int i=0;i<5;i++){
            Map<Integer,Object> map=new HashMap<>();
            map.put(1,"xx"+i);
            map.put(2,new Timestamp(System.currentTimeMillis()));
            map.put(3,new Timestamp(System.currentTimeMillis()));
            map.put(4,new Timestamp(System.currentTimeMillis()));
            map.put(5,i);
            lst.add(map);
        }
        int[] result=jdbcHelper.batchUpdate(sql,lst);
        for(int item:result){
            System.out.println(item);
        }
    }
}
