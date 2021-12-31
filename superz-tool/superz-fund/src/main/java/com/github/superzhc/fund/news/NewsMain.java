package com.github.superzhc.fund.news;

import com.github.superzhc.data.tushare.TusharePro;

/**
 * @author superz
 * @create 2021/12/31 10:12
 */
public class NewsMain {
    public static void main(String[] args) {
        String token = "xxx";
        TusharePro pro = new TusharePro(token);

        System.out.println(pro.execute("major_news", null, "title,content,pub_time,src"));
    }
}
