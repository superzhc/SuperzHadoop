package com.github.superzhc.sql.parser.lineage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

/**
 * 数据血缘分析
 *
 * @author superz
 * @create 2022/6/17 15:43
 **/
public class DataLineageAnalyzer {
    public static void analyze(String sql) {
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
    }
}
