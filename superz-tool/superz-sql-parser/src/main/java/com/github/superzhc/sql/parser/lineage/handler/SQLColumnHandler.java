package com.github.superzhc.sql.parser.lineage.handler;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author superz
 * @create 2022/6/20 19:23
 **/
public class SQLColumnHandler {
    private static final Logger log = LoggerFactory.getLogger(SQLColumnHandler.class);

    public static void handle(SQLSelectItem column) {
        SQLExpr columnExpr = column.getExpr();
        handle(columnExpr);
        String alias = column.getAlias();
    }

    public static void handle(SQLExpr columnExpr) {
        String columnName;
        String owner = null;
        if (columnExpr instanceof SQLIdentifierExpr) {
            columnName = ((SQLIdentifierExpr) columnExpr).getName();
        } else if (columnExpr instanceof SQLPropertyExpr) {
            SQLPropertyExpr columnProperty = (SQLPropertyExpr) columnExpr;
            columnName = columnProperty.getName();
            owner = columnProperty.getOwnerName();
        } else if (columnExpr instanceof SQLAggregateExpr) {
            SQLAggregateExpr columnAggregate = (SQLAggregateExpr) columnExpr;
            List<SQLExpr> args = columnAggregate.getArguments();

            columnName = SQLUtils.toSQLString(columnExpr);
        } else if (columnExpr instanceof SQLMethodInvokeExpr) {
            SQLMethodInvokeExpr columnMethod = (SQLMethodInvokeExpr) columnExpr;
            List<SQLExpr> args = columnMethod.getArguments();

            columnName = SQLUtils.toSQLString(columnMethod);
        } else {
            log.warn("Column[{}] `{}` Unsupported!", columnExpr.getClass().getName(), SQLUtils.toSQLString(columnExpr));
            columnName = SQLUtils.toSQLString(columnExpr);
        }

    }
}
