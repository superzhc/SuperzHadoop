package com.github.superzhc.marco.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/7/5 14:46
 **/
public class MarcoMain {
    public static void main(String[] args) {
        Table table = EastMoneyMarco.qyspjg();

        System.out.println(table.print());
        System.out.println(table.shape());
        //System.out.println(table.structure().printAll());
    }
}
