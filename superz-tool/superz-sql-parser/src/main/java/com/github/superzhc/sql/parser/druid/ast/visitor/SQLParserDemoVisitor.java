package com.github.superzhc.sql.parser.druid.ast.visitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInputOutputFormat;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLAlterResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLCreateResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLListResourceGroupStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2022/6/21 9:54
 **/
public class SQLParserDemoVisitor /*extends SQLASTVisitorAdapter*/ implements SQLASTVisitor {
    private static final Logger log = LoggerFactory.getLogger(SQLParserDemoVisitor.class);

    @Override
    public void endVisit(SQLAllColumnExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLBetweenExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLBinaryOpExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLCaseExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLCaseExpr.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLCaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLCaseStatement.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLCharExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLIdentifierExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLInListExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLIntegerExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLSmallIntExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLBigIntExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLTinyIntExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLExistsExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLNCharExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLNotExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLNullExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLNumberExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLRealExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLPropertyExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLSelectGroupByClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLSelectItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLSelectStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void postVisit(SQLObject x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.postVisit(x);
    }

    @Override
    public void preVisit(SQLObject x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.preVisit(x);
    }

    @Override
    public boolean visit(SQLAllColumnExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLBetweenExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLBinaryOpExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCaseExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCaseExpr.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCaseStatement.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCastExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCharExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLExistsExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLIdentifierExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLInListExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLIntegerExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLSmallIntExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLBigIntExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLTinyIntExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLNCharExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLNotExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLNullExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLNumberExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLRealExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLPropertyExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLSelectGroupByClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLSelectItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCastExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSelectStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAggregateExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAggregateExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLVariantRefExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLVariantRefExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLQueryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLQueryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUnaryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUnaryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLHexExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLHexExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSelect x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSelect select) {
        log.debug("{}:{}", select.getClass().getName(), SQLUtils.toSQLString(select));
        SQLASTVisitor.super.endVisit(select);
    }

    @Override
    public boolean visit(SQLSelectQueryBlock x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSelectQueryBlock x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExprTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExprTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLOrderBy x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLOrderBy x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLZOrderBy x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLZOrderBy x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSelectOrderByItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSelectOrderByItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLColumnDefinition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLColumnDefinition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLColumnDefinition.Identity x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLColumnDefinition.Identity x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCharacterDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCharacterDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDeleteStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDeleteStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCurrentOfCursorExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCurrentOfCursorExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLInsertStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLInsertStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLInsertStatement.ValuesClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLInsertStatement.ValuesClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUpdateSetItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUpdateSetItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUpdateStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUpdateStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateViewStatement.Column x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateViewStatement.Column x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLNotNullConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLNotNullConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLMethodInvokeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLMethodInvokeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUnionQuery x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUnionQuery x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSetStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSetStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAssignItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAssignItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCallStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCallStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLJoinTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLJoinTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLJoinTableSource.UDJ x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLJoinTableSource.UDJ x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSomeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSomeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAnyExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAnyExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAllExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAllExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLInSubQueryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLInSubQueryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLListExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLListExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSubqueryTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSubqueryTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLTruncateStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLTruncateStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDefaultExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDefaultExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCommentStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCommentStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDeleteByCondition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDeleteByCondition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableModifyClusteredBy x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableModifyClusteredBy x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropColumnItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropColumnItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterSystemSetConfigStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterSystemSetConfigStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterSystemGetConfigStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterSystemGetConfigStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropIndexStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropIndexStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSavePointStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSavePointStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRollbackStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRollbackStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLReleaseSavePointStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLReleaseSavePointStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLCommentHint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCommentHint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLOver x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLOver x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLKeep x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLKeep x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLColumnPrimaryKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLColumnPrimaryKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLColumnUniqueKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLColumnUniqueKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLWithSubqueryClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLWithSubqueryClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLWithSubqueryClause.Entry x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLWithSubqueryClause.Entry x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAlterColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAlterColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCheck x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCheck x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDefault x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDefault x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropForeignKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropForeignKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropPrimaryKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropPrimaryKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDisableKeys x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDisableKeys x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableEnableKeys x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableEnableKeys x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDisableConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDisableConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableEnableConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableEnableConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLColumnCheck x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLColumnCheck x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExprHint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExprHint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUnique x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUnique x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPrimaryKeyImpl x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPrimaryKeyImpl x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateIndexStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateIndexStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRenameColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRenameColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLColumnReference x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLColumnReference x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLForeignKeyImpl x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLForeignKeyImpl x) {
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropSequenceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropSequenceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropTriggerStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropTriggerStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLDropUserStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropUserStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExplainStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExplainStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLGrantStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLGrantStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLIndexOptions x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLIndexOptions x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLIndexDefinition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLIndexDefinition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAlterIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAlterIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateTriggerStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateTriggerStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropFunctionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropFunctionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropTableSpaceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropTableSpaceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropProcedureStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropProcedureStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLBooleanExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLBooleanExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUnionQueryTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUnionQueryTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLTimestampExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLTimestampExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDateTimeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDateTimeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDoubleExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDoubleExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLFloatExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLFloatExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRevokeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRevokeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLBinaryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLBinaryExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRename x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRename x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterViewRenameStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterViewRenameStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowTablesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowTablesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddExtPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddExtPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropExtPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropExtPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRenamePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRenamePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableSetComment x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableSetComment x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableSetLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPrivilegeItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPrivilegeItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableSetLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableEnableLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableSetLocation x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableSetLocation x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableEnableLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTablePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTablePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTablePartitionSetProperties x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTablePartitionSetProperties x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDisableLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDisableLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableTouch x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableTouch x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLArrayExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLArrayExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLOpenStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLOpenStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLFetchStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLFetchStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCloseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCloseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLGroupingSetExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLGroupingSetExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLIfStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLIfStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLIfStatement.ElseIf x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLIfStatement.ElseIf x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLIfStatement.Else x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLIfStatement.Else x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLLoopStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLLoopStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLParameter x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLParameter x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateProcedureStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateProcedureStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateFunctionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateFunctionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLBlockStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLBlockStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDeclareItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDeclareItem x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionValue x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionValue x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionByRange x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionByRange x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionByHash x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionByHash x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionByList x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionByList x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSubPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSubPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSubPartitionByHash x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSubPartitionByHash x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSubPartitionByRange x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSubPartitionByRange x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSubPartitionByList x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSubPartitionByList x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableConvertCharSet x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableConvertCharSet x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableReOrganizePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableReOrganizePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableCoalescePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableCoalescePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableTruncatePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableTruncatePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDiscardPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDiscardPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableImportPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableImportPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAnalyzePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAnalyzePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableCheckPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableCheckPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableOptimizePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableOptimizePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRebuildPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRebuildPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRepairPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRepairPartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSequenceExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSequenceExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLMergeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLMergeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLMergeStatement.MergeUpdateClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLMergeStatement.MergeUpdateClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLMergeStatement.MergeInsertClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLMergeStatement.MergeInsertClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLErrorLoggingClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLErrorLoggingClause x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLNullConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLNullConstraint x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateSequenceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateSequenceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDateExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDateExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLLimit x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLLimit x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLStartTransactionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLStartTransactionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDescribeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDescribeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLWhileStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLWhileStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDeclareStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDeclareStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLReturnStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLReturnStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLArgument x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLArgument x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCommitStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCommitStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLFlashbackExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLFlashbackExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowCreateMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowCreateMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLBinaryOpExprGroup x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLBinaryOpExprGroup x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLScriptCommitStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLScriptCommitStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLReplaceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLReplaceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateUserStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateUserStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterFunctionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterFunctionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTypeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTypeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLIntervalExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLIntervalExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLLateralViewTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLLateralViewTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowErrorsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowErrorsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowGrantsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowGrantsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowPackagesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowPackagesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowRecylebinStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowRecylebinStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterCharacter x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterCharacter x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExprStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExprStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterProcedureStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterProcedureStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropEventStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropEventStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropLogFileGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropLogFileGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropServerStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropServerStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropSynonymStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropSynonymStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRecordDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRecordDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropTypeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropTypeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExternalRecordFormat x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExternalRecordFormat x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLArrayDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLArrayDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLMapDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLMapDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLStructDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLStructDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRowDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRowDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLStructDataType.Field x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLStructDataType.Field x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRefreshMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRefreshMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterMaterializedViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateTableGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateTableGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropTableGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropTableGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableSubpartitionAvailablePartitionNum x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableSubpartitionAvailablePartitionNum x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLShowDatabasesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowDatabasesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowTableGroupsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowTableGroupsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowColumnsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowColumnsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowCreateTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowCreateTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowProcessListStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowProcessListStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableSetOption x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableSetOption x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLShowCreateViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowCreateViewStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowViewsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowViewsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRenameIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRenameIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterSequenceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterSequenceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableExchangePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableExchangePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateRoleStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateRoleStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropRoleStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropRoleStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableReplaceColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableReplaceColumn x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLMatchAgainstExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLMatchAgainstExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLTimeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLTimeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropCatalogStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropCatalogStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLShowPartitionsStmt x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowPartitionsStmt x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLValuesExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLValuesExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLContainsExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLContainsExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDumpStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDumpStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLValuesTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLValuesTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExtractExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExtractExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLWindow x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLWindow x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLJSONExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLJSONExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDecimalExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDecimalExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAnnIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAnnIndex x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUnionDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUnionDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableRecoverPartitions x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableRecoverPartitions x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterIndexStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterIndexStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLAlterIndexStatement.Rebuild x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterIndexStatement.Rebuild x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowIndexesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowIndexesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAnalyzeTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAnalyzeTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExportTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExportTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLImportTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLImportTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLTableSampling x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLTableSampling x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSizeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSizeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableArchivePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableArchivePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableUnarchivePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableUnarchivePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCreateOutlineStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateOutlineStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropOutlineStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropOutlineStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterOutlineStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterOutlineStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowOutlinesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowOutlinesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPurgeTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPurgeTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPurgeTemporaryOutputStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPurgeTemporaryOutputStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPurgeLogsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPurgeLogsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPurgeRecyclebinStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPurgeRecyclebinStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowStatisticStmt x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowStatisticStmt x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowStatisticListStmt x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowStatisticListStmt x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddSupplemental x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddSupplemental x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowCatalogsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowCatalogsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowFunctionsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowFunctionsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowSessionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowSessionStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDbLinkExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDbLinkExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCurrentTimeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCurrentTimeExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCurrentUserExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCurrentUserExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowQueryTaskStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowQueryTaskStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAdhocTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAdhocTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(HiveCreateTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(HiveCreateTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(HiveInputOutputFormat x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(HiveInputOutputFormat x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExplainAnalyzeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExplainAnalyzeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionRef x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionRef x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionRef.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionRef.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLWhoamiStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLWhoamiStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropResourceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDropResourceStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLForStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLForStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLUnnestTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLUnnestTableSource x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCopyFromStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCopyFromStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowUsersStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowUsersStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSubmitJobStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSubmitJobStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLTableLike x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLTableLike x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLSyncMetaStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLSyncMetaStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLValuesQuery x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLValuesQuery x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLDataTypeRefExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDataTypeRefExpr x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLArchiveTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLArchiveTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLBackupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLBackupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRestoreStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRestoreStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLBuildTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLBuildTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCancelJobStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCancelJobStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLExportDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLExportDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLImportDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLImportDatabaseStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLRenameUserStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLRenameUserStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionByValue x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionByValue x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTablePartitionCount x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTablePartitionCount x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableBlockSize x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableBlockSize x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableCompression x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableCompression x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTablePartitionLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTablePartitionLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableSubpartitionLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableSubpartitionLifecycle x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropSubpartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropSubpartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableDropClusteringKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableDropClusteringKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableAddClusteringKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableAddClusteringKey x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(MySqlKillStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(MySqlKillStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLCreateResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCreateResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public void endVisit(SQLDropResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLDropResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLListResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLListResourceGroupStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableMergePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableMergePartition x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionSpec x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionSpec x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLPartitionSpec.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLPartitionSpec.Item x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLAlterTableChangeOwner x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLAlterTableChangeOwner x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLTableDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLTableDataType x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLCloneTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLCloneTableStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowHistoryStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowHistoryStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowRoleStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowRoleStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowRolesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowRolesStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public boolean visit(SQLShowVariantsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowVariantsStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLShowACLStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLShowACLStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    @Override
    public boolean visit(SQLOptimizeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        return SQLASTVisitor.super.visit(x);
    }

    @Override
    public void endVisit(SQLOptimizeStatement x) {
        log.debug("{}:{}", x.getClass().getName(), SQLUtils.toSQLString(x));
        SQLASTVisitor.super.endVisit(x);
    }

    public static void main(String[] args) {
        String sql = "select * from (select T1.sid,class1,class2 from (select sid,score as class1 from sc) t1,(select sid,score class2 from sc) t2 where t1.sid=t2.sid)";

        SQLStatement statement = SQLUtils.parseSingleMysqlStatement(sql);

        SQLParserDemoVisitor visitor = new SQLParserDemoVisitor();
        statement.accept(visitor);
    }
}
