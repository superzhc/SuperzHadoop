package com.github.superzhc.financial.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * 不可用
 *
 * @author superz
 * @create 2022/5/6 19:12
 **/
public class CNIndex {
    public static Table indices() {
        List<Map<String,Object>> data= com.github.superzhc.data.index.CNIndex.indices();

        Table table = TableUtils.buildByMap(data);

        // List<String> finalColumnNames = Arrays.asList(
//                "_",
//                "_",
//                "指数代码",
//                "_",
//                "_",
//                "_",
//                "_",
//                "_",
//                "指数简称",
//                "_",
//                "_",
//                "_",
//                "样本数",
//                "收盘点位",
//                "涨跌幅",
//                "_",
//                "PE滚动",
//                "_",
//                "成交量",
//                "成交额",
//                "总市值",
//                "自由流通市值",
//                "_",
//                 "_"
        // );
        // table = TableUtils.rename(table, finalColumnNames);

        return table;
    }

    public static Table history(String symbol) {
        List<Map<String, Object>> data= com.github.superzhc.data.index.CNIndex.history(symbol);

        Table table = TableUtils.buildByMap(data);
        return table;
    }

    /*public static Table sample(String symbol){
        // 返回 excel 文件流
        String url="http://www.cnindex.com.cn/sample-detail/download";

        Map<String,Object> params=new HashMap<>();
        params.put("indexcode",transform(symbol));
        params.put("dateStr", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    }*/

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return ss[0];
    }

    public static void main(String[] args) {
        Table table = indices();
//        Table table = history("399001");

        System.out.println(table.print());
        System.out.println(table.shape());
//        System.out.println(table.structure().print());
    }
}
