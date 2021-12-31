package com.github.superzhc.fund.index;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.tushare.TusharePro;
import com.github.superzhc.data.tushare.TushareResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 获取指数基本信息入库
 *
 * @author superz
 * @create 2021/12/30 17:55
 */
public class FetchIndexBasic {
    private static final String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    private static final String username = "root";
    private static final String password = "123456";
    private static final String table = "fund_index_basic";

    public static void main(String[] args) {
        String token = "xxx";
        TusharePro pro = new TusharePro(token);

        Integer limit = 1000;
        Integer offset = 1;
        boolean hasMore = true;

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            String[] columns = jdbc.columns(table);

            while (hasMore) {
                Map<String, String> params = new HashMap<>();
                params.put("limit", String.valueOf(limit));
                params.put("offset", String.valueOf(offset));
                hasMore = pro.execute0("index_basic", params, columns, new Function<TushareResponse.Data, Boolean>() {
                    @Override
                    public Boolean apply(TushareResponse.Data data) {

                        //jdbc.batchUpdate(table, data.getFields(), data.getItems());
                        // 字段存在关键字，自行构建 insert 语句
                        jdbc.batchUpdate("INSERT INTO fund_index_basic(ts_code,name,fullname,market,publisher,index_type,category,base_date,base_point,list_date,weight_rule,`desc`,exp_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", data.getItems());

                        return data.getHasMore();
                    }
                });

                offset += limit;
            }
        }
    }
}
