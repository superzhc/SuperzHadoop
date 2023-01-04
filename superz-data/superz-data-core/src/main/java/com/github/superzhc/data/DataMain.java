package com.github.superzhc.data;

import com.github.superzhc.common.email.MailHelper;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.fund.DoctorXiong;
import com.github.superzhc.data.fund.EastMoneyFund;
import com.github.superzhc.data.fund.SinaFund;
import com.github.superzhc.data.news.MoFish;
import com.github.superzhc.data.shopping.GuangDiu;
import com.github.superzhc.data.stock.NetEaseStock;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/7 23:28
 */
public class DataMain {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false&characterEncoding=utf8";
        String username = "root";
        String password = "123456";

        // // 创建基金表
        // String sql="create table em_funds(id bigint auto_increment primary key,code varchar(8) not null,name varchar(255) not null,type varchar(255) not null,pinyin varchar(255) null,full_pinyin varchar(255) null)";
        // String sql="create table sina_funds(id bigint auto_increment primary key,code varchar(8) not null,name varchar(255) not null,company_name varchar(255),subject_name varchar(255),create_date varchar(50),scale double,cxpj int,htpj int,jajxpj int,zspj int,yhpj3 int,yhpj5 int)";


        // try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            // String sql = "insert into em_funds (code,name,type,pinyin,full_pinyin) values (?,?,?,?,?)";
            // List<Map<String, Object>> data = EastMoneyFund.funds();
            // jdbc.batchUpdate(sql, MapUtils.values(data, "code", "name", "type", "pinyin", "full_pinyin"));

            // String sql = "insert into sina_funds(code, name, company_name, subject_name, create_date, scale, cxpj, htpj, jajxpj, zspj, yhpj3, yhpj5) VALUE (?,?,?,?,?,?,?,?,?,?,?,?)";
            // List<Map<String, Object>> data = SinaFund.funds();
            // jdbc.batchUpdate(sql, MapUtils.values(data, "code", "name", "company_name", "subject_name", "create_date", "scale", "cxpj", "htpj", "jajxpj", "zspj", "yhpj3", "yhpj5"));

        System.out.println(MapUtils.print(GuangDiu.all()));

        MailHelper mailHelper = MailHelper.qq("1013849463@qq.com", "vvtvtlpssnkfbfjd");
        mailHelper.from("易")
                .to("zhengchao0555@163.com")
                .subject("测试邮件")
                .html("<h3>HTML部分</h3>")
                .text("TEXT部分")
                .send();

        // }
    }
}
