package com.github.superzhc.data;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.news.MoFish;
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

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            String sql = "insert into mofish_20221011 ( id, create_time, comment_num, approval_num, title, hot_desc, url, img_url, type_name)\n" +
                    " values (?,?,?,?,?,?,?,?,?)";

            List<Map<String, Object>> data = MoFish.taobao3760();
            jdbc.batchUpdate(sql,MapUtils.values(data,"id","CreateTime","commentNum","approvalNum","Title","hotDesc","Url","imgUrl","TypeName"));
        }

    }
}
