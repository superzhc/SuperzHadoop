package com.github.superzhc.data.demo;

import com.github.superzhc.data.common.FileData;
import com.github.superzhc.data.file.TxtData;

/**
 * @author superz
 * @create 2022/1/20 1:15
 */
public class FileDataDemo {
    private static final String TG_PATH = "D:\\download\\telegram\\";

    static String url = "jdbc:mysql://localhost:3306/home_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    static String username = "root";
    static String password = "123456";

    public static void main(String[] args) {
        pharmacy();
    }

    // 药店
    static void pharmacy() {
        String fileName = "全国车主76万2020年.xlsx";

        FileData fileData=FileData.file(TG_PATH+fileName);
        fileData.preview(1000);
        fileData.count();

        //TxtData txtData = new TxtData(TG_PATH + fileName, "GB2312", 1);
        //txtData.preview();
        //txtData.count();

//        String table = "pharmacy";
//        String[] columns = "sdate,enable,constellation1,cardname,cardno,age,channelid,integral,sex,phone,tenantid,iconpath,cardid,channelname,constellation,openid,headimgurl,pct,countbuynum,membertotalvalue,lastbuytime,channelnos0,customername,cityNm,provinceNm,cityid,provinceid,channelnos1,channelnos2,memberdimensionid,channelnos3,channelnos4,channelnos5,channelnos6,channelnos7".split(",");
//        txtData.write2db(url, username, password, table, columns, ",");
    }
}
