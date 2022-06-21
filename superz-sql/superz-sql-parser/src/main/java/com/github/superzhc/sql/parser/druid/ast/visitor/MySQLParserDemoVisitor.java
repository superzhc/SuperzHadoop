package com.github.superzhc.sql.parser.druid.ast.visitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 暂不继承指定方言的 visitor
 *
 * @author superz
 * @create 2022/6/21 15:51
 **/
public class MySQLParserDemoVisitor implements SQLASTVisitor {
    private static final Logger log = LoggerFactory.getLogger(MySQLParserDemoVisitor.class);

    public boolean visit(SQLSelectQueryBlock x) {
        log.debug(SQLUtils.toSQLString(x));
        return true;
    }

    public boolean visit(SQLExprTableSource x) {
        if (null != x.getSchema()) {
            log.debug("表：{}.{}", x.getSchema(), x.getTableName());
        } else {
            log.debug("表：{}", x.getTableName());
        }
        return true;
    }

    public boolean visit(SQLColumnDefinition x) {
        SQLDataType dataType = x.getDataType();
        log.debug("列名:{},数据类型:{}", x.getName(), dataType.getName());
        return true;
    }

    public boolean visit(SQLInsertStatement.ValuesClause x) {
        log.debug("插入数据");
        return true;
    }


    public static void main(String[] args) {
        String sql1_1 = "CREATE TABLE IF NOT EXISTS `runoob_tbl`(\n" +
                "   `runoob_id` INT UNSIGNED AUTO_INCREMENT,\n" +
                "   `runoob_title` VARCHAR(100) NOT NULL,\n" +
                "   `runoob_author` VARCHAR(40) NOT NULL,\n" +
                "   `submission_date` DATE,\n" +
                "   PRIMARY KEY ( `runoob_id` )\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        String sql2_1 = "DROP TABLE runoob_tbl;";

        String sql3_1="ALTER TABLE testalter_tbl  DROP i;";
        String sql3_2="ALTER TABLE testalter_tbl ADD i INT;";
        String sql3_3="ALTER TABLE testalter_tbl ADD i INT FIRST;";
        String sql3_4="ALTER TABLE testalter_tbl ADD i INT AFTER c;";
        String sql3_5="ALTER TABLE testalter_tbl MODIFY c CHAR(10);";
        String sql3_6="ALTER TABLE testalter_tbl CHANGE i j BIGINT;";
        String sql3_7="ALTER TABLE testalter_tbl CHANGE j j INT;";
        String sql3_8="ALTER TABLE testalter_tbl MODIFY j BIGINT NOT NULL DEFAULT 100;";
        String sql3_9="ALTER TABLE testalter_tbl ALTER i SET DEFAULT 1000;";
        String sql3_10="ALTER TABLE testalter_tbl RENAME TO alter_tbl;";


        String sql4_1 = "INSERT INTO runoob_tbl(runoob_title, runoob_author, submission_date) VALUES('学习 PHP', '菜鸟教程', NOW())";
        String sql4_2 = "INSERT INTO `employee_tbl` VALUES ('1', '小明', '2016-04-22 15:25:33', '1'), ('2', '小王', '2016-04-20 15:25:47', '3'), ('3', '小丽', '2016-04-19 15:26:02', '2'), ('4', '小王', '2016-04-07 15:26:14', '4'), ('5', '小明', '2016-04-11 15:26:40', '4'), ('6', '小明', '2016-04-04 15:26:54', '2');";

        String sql5_1 = "UPDATE runoob_tbl SET runoob_title='学习 C++' WHERE runoob_id=3;";

        String sql6_1 = "DELETE FROM runoob_tbl WHERE runoob_id=3;";

        String sql7_1 = "select * from runoob_tbl;";
        String sql7_2 = "SELECT * from runoob_tbl WHERE runoob_author='菜鸟教程';";
        String sql7_3 = "SELECT * from runoob_tbl  WHERE runoob_author LIKE '%COM';";
        String sql7_4 = "SELECT country FROM Websites\n" +
                "UNION\n" +
                "SELECT country FROM apps\n" +
                "ORDER BY country;";
        String sql7_5 = "SELECT name, COUNT(*) FROM   employee_tbl GROUP BY name;";
        String sql7_6 = "SELECT name, SUM(signin) as signin_count FROM  employee_tbl GROUP BY name WITH ROLLUP;";
        String sql7_7 = "SELECT a.runoob_id, a.runoob_author, b.runoob_count FROM runoob_tbl a INNER JOIN tcount_tbl b ON a.runoob_author = b.runoob_author;";
        // 等价于上面的语句
        String sql7_8 = "SELECT a.runoob_id, a.runoob_author, b.runoob_count FROM runoob_tbl a, tcount_tbl b WHERE a.runoob_author = b.runoob_author;";
        String sql7_9 = "SELECT a.runoob_id, a.runoob_author, b.runoob_count FROM runoob_tbl a LEFT JOIN tcount_tbl b ON a.runoob_author = b.runoob_author;";
        String sql7_10 = "SELECT a.runoob_id, a.runoob_author, b.runoob_count FROM runoob_tbl a RIGHT JOIN tcount_tbl b ON a.runoob_author = b.runoob_author;";
        String sql7_11 = "SELECT * FROM runoob_test_tbl WHERE runoob_count IS NULL";
        String sql7_12 = "SELECT name FROM person_tbl WHERE name REGEXP '^[aeiou]|ok$';";

        SQLStatement statement = SQLUtils.parseSingleMysqlStatement(sql7_4);
        statement.accept(new MySQLParserDemoVisitor());
    }
}
