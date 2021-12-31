package com.github.superzhc.data.file.demo;

import com.github.superzhc.data.file.ExcelData;

import java.util.Arrays;

/**
 * @author superz
 * @create 2021/12/30 14:13
 */
public class CarMain {
    private static final String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    private static final String username = "root";
    private static final String password = "123456";

    public static void main(String[] args) {
        String path = "D:\\downloads\\baidu\\car\\数据包五\\江苏\\2014\\江苏,1000.xlsx";

        ExcelData data = new ExcelData(path);
         data.preview();
        data.count();

        //data.append2db(url, username, password, "car_jiangsu_2014", "chexing,chepaihao,pinpai,c1,cheqi,yanse,vin,c2,shenfenzheng,lianxiren,address,c3,lianxidianhua".split(","), 0);
        //data.append2db(url,username,password,"car_jiangsu_2014","chexing,chepaihao,pinpai,c1,cheqi,vin,c2,shenfenzheng,lianxiren,address,c3,lianxidianhua".split(","),0,1);

        data.append2db(url,username,password,"car_jiangsu_2014","chexing,chepaihao,pinpai,c1,cheqi,yanse,vin,c2,shenfenzheng,lianxiren,address,c3,lianxidianhua,riqi".split(","),0);
        // data.append2db(url,username,password,"car_jiangsu_2014","chepaihao,pinpai,".split(","),0);
    }
}
