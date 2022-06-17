package com.github.superzhc.sql.parser.lineage.handler;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.*;
import com.github.superzhc.sql.parser.lineage.entity.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * select语句
 *
 * @author superz
 * @create 2022/6/17 15:45
 **/
public class SelectStatementHandler {
    private static final Logger log = LoggerFactory.getLogger(SelectStatementHandler.class);

    public static Table handle(SQLSelectStatement sqlSelectStatement) {
        SQLSelect sqlSelect = sqlSelectStatement.getSelect();
        return handle(sqlSelect);
    }

    public static Table handle(SQLSelect sqlSelect) {
        SQLSelectQuery query = sqlSelect.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            /**
             * 形如：
             * 1. select * from t1
             * 2. select c1,c2,c3 from t1
             */
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) query;
            return handle(sqlSelectQueryBlock);
        } else if (query instanceof SQLUnionQuery) {
            log.warn("尚未实现 SQLUnionQuery 分析：{}", SQLUtils.toSQLString(query));
        } else {
            log.error("当前类型{}尚未实现分析：{}", query.getClass().getName(), SQLUtils.toSQLString(query));
        }
        return null;
    }

    public static Table handle(SQLSelectQueryBlock query) {
        // 获取查询的表
        SQLTableSource tableSource = query.getFrom();
        Table table = SQLTableSourceHandler.handle(tableSource);

        // 获取字段
        List<SQLSelectItem> columns = query.getSelectList();
        if (null != columns && columns.size() > 0) {
            // 窄化
            List<String> oldFields = table.clearFields();

            for (SQLSelectItem column : columns) {
                String columnName = SQLUtils.toSQLString(column.getExpr());
                String alias = column.getAlias();
                // select 语句中字段设置了别名，则输出会是以别名的方式显示，原始的列并不会显示
                if ("*".equals(columnName)) {
                    table.addFields(oldFields);
                    continue;
                }

                if (null != alias && alias.length() > 0) {
                    table.addField(alias);
                    continue;
                }

                table.addField(columnName);
            }
        }

        log.debug("{}", table);
        return table;
    }
}
