package com.github.superzhc.sql.parser.druid.usage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.*;

/**
 * @author superz
 * @create 2022/6/15 14:41
 **/
public class SQLCreateTableStatementUsage {
    public static void usage(SQLCreateTableStatement statement) {
        SQLTableSource tableSource = statement.getTableSource();
        System.out.println("新建表：" + SQLUtils.toSQLString(tableSource));

        SQLSelect sqlSelect = statement.getSelect();
        if (null != sqlSelect) {
            SQLSelectStatementUsage.usage(sqlSelect);
        }
    }
}
