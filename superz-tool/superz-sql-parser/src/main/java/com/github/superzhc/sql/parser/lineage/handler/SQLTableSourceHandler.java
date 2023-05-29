package com.github.superzhc.sql.parser.lineage.handler;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.*;
import com.github.superzhc.sql.parser.lineage.entity.Column;
import com.github.superzhc.sql.parser.lineage.entity.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 2022年6月20日 modify 在数据源处获取字段元数据
 *
 * @author superz
 * @create 2022/6/17 16:26
 **/
public class SQLTableSourceHandler {
    private static final Logger log = LoggerFactory.getLogger(SQLTableSourceHandler.class);

    public static Table handle(SQLTableSource tableSource) {
        Table t = null;
        if (tableSource instanceof SQLExprTableSource) {
            /**
             * 形如：
             * 1. table
             * 2. table1 as t1
             * 3. table1 t1
             */
            SQLExprTableSource table = (SQLExprTableSource) tableSource;
            String tableName = table.getTableName();
            String alias = table.getAlias();

            // TODO:获取表的元数据

            t = new Table(tableName, alias, SQLUtils.toSQLString(table));
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            /**
             * 形如：
             * 1. (select * from t1) t2
             * 2. (select c1,c2,c3 from t1) as t2
             */
            SQLSubqueryTableSource table = (SQLSubqueryTableSource) tableSource;
            String alias = table.getAlias();

            Table subTable = SelectStatementHandler.handle(table.getSelect());
            for (Column column : subTable.getColumns()) {
                column.setTableAlias(alias);
            }

            t = new Table(/*Table.tempTable()*/null, alias, subTable.getColumns(), SQLUtils.toSQLString(table));
            t.addDependenceTable(subTable.getName()).addDependenceTables(subTable.getDependenceTables());
        } else if (tableSource instanceof SQLJoinTableSource) {
            /**
             * 形如：
             * 1. t1 join t2
             * 2. t1 join (select * from t2)
             */
            SQLJoinTableSource table = (SQLJoinTableSource) tableSource;

//            List<String> fields = new ArrayList<>();
            List<Column> columns = new ArrayList<>();

            SQLTableSource leftTable = table.getLeft();
            Table leftT = handle(leftTable);
            if (leftT.getColumns().size() > 0) {
                columns.addAll(leftT.getColumns());
            }

            SQLTableSource rightTable = table.getRight();
            Table rightT = handle(rightTable);
            if (rightT.getColumns().size() > 0) {
                columns.addAll(rightT.getColumns());
            }

            t = new Table(null/*Table.tempTable()*/, SQLUtils.toSQLString(tableSource));
            t.addDependenceTable(leftT.getName()).addDependenceTables(leftT.getDependenceTables())
                    .addDependenceTable(rightT.getName()).addDependenceTables(rightT.getDependenceTables());
            t.setColumns(columns);
        } else {
            log.error("当前表类型{}尚未实现分析：{}", tableSource.getClass().getName(), SQLUtils.toSQLString(tableSource));
        }
        if (null != t) {
            log.debug("{}", t);
        }
        return t;
    }
}
