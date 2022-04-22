package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.List;

/**
 * @author superz
 * @create 2022/4/20 14:01
 **/
public class DanJuanFunds {

    public static Table indexEva(){
        String url="https://danjuanfunds.com/djapi/index_eva/dj";
        String result=HttpRequest.get(url).body();
        JsonNode json= JsonUtils.json(result,"data","items");

        List<String> columnNames=JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows=JsonUtils.extractObjectData(json,columnNames);

        Table table= TableUtils.build(columnNames,dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table=indexEva();
        System.out.println(table.structure().printAll());
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
