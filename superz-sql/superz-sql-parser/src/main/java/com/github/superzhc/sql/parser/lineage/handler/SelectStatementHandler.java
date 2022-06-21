package com.github.superzhc.sql.parser.lineage.handler;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.github.superzhc.sql.parser.lineage.entity.Column;
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
        table.setExpr(SQLUtils.toSQLString(query));

        // 获取字段
        List<SQLSelectItem> columns = query.getSelectList();
        if (null != columns && columns.size() > 0) {
            // 窄化，先获取表的字段信息，并清空所有字段，根据 select 字段来选择的字段
            List<Column> oldFields = table.clearColumns();

            label:
            for (SQLSelectItem column : columns) {
                SQLExpr columnExpr = column.getExpr();

                String columnName;
                String owner = null;
                if (columnExpr instanceof SQLIdentifierExpr) {
                    columnName = ((SQLIdentifierExpr) columnExpr).getName();
                } else if (columnExpr instanceof SQLPropertyExpr) {
                    SQLPropertyExpr columnProperty = (SQLPropertyExpr) columnExpr;
                    columnName = columnProperty.getName();
                    owner = columnProperty.getOwnerName();
                } else {
                    log.warn("Column[{}] `{}` Unsupported!", columnExpr.getClass().getName(), SQLUtils.toSQLString(columnExpr));
                    columnName = SQLUtils.toSQLString(columnExpr);
                }
                String alias = column.getAlias();

                // select 语句中字段设置了别名，则输出会是以别名的方式显示，原始的列并不会显示
                if ("*".equals(columnName)) {
                    table.addColumns(oldFields);
                    continue;
                }

                for (Column oldField : oldFields) {
                    // 如果选择的字段为 t1.c1 的格式，t1 即为 owner
                    if (null != owner) {
                        /**
                         * t1 可能是表的别名，也可能是表名，优先别名来匹配
                         */
                        // 别名匹配
                        if (oldField.getTableAlias() != null && owner.equalsIgnoreCase(oldField.getTableAlias())) {
                            /**
                             * c1 也可能是字段的别名，也可能是实际字段名，优先别名匹配
                             */
                            // 字段别名匹配
                            if (oldField.getAlias() != null && oldField.getAlias().equalsIgnoreCase(columnName)) {
                                table.addColumn(oldField);
                                continue label;
                            }

                            // 字段名匹配
                            if (oldField.getAlias() == null && oldField.getName().equalsIgnoreCase(columnName)) {
                                table.addColumn(oldField);
                                continue label;
                            }
                        }

                        // 表名匹配
                        if (oldField.getTable() != null && owner.equalsIgnoreCase(oldField.getTable())) {
                            if (oldField.getAlias() != null && oldField.getAlias().equalsIgnoreCase(columnName)) {
                                table.addColumn(oldField);
                                continue label;
                            }

                            if (oldField.getAlias() == null && oldField.getName().equalsIgnoreCase(columnName)) {
                                table.addColumn(oldField);
                                continue label;
                            }
                        }
                    }

                    if (oldField.getAlias() != null && oldField.getAlias().equalsIgnoreCase(columnName)) {
                        table.addColumn(oldField);
                        continue label;
                    }

                    if (oldField.getAlias() == null && oldField.getName().equalsIgnoreCase(columnName)) {
                        table.addColumn(oldField);
                        continue label;
                    }
                }

                // 此处表示未匹配任何字段，若已知字段元数据的情况下，这个SQL是有问题的
                Column field = new Column(table.getName(), table.getAlias(), columnName, alias);
                table.addColumn(field);
            }
        } else {
            for (Column fields : table.getColumns()) {
                if (null != table.getName()) {
                    fields.setTable(table.getName());
                }

                if (null != table.getAlias()) {
                    fields.setTableAlias(table.getAlias());
                }
            }
        }

        log.debug("{}", table);
        return table;
    }
}
