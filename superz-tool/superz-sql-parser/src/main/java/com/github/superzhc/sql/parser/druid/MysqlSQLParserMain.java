package com.github.superzhc.sql.parser.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.github.superzhc.sql.parser.druid.usage.SQLStatementUsage;
import com.github.superzhc.sql.parser.lineage.handler.SQLTableSourceHandler;
import com.github.superzhc.sql.parser.lineage.handler.SelectStatementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * @author superz
 * @create 2022/6/14 14:09
 **/
public class MysqlSQLParserMain {
    private static final Logger log = LoggerFactory.getLogger(MysqlSQLParserMain.class);

    private static final DbType DB_TYPE = JdbcConstants.MYSQL;

    public static void main(String[] args) {
        /* 创建表 */
        String sql1_1 = "CREATE TABLE `users` (\n" +
                "  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',\n" +
                "  `age` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '年龄',\n" +
                "  `gender` enum('male','female') COLLATE utf8mb4_unicode_ci NOT NULL,\n" +
                "  `salary` int(10) unsigned NOT NULL DEFAULT '2000',\n" +
                "  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',\n" +
                "  `birthday` date NOT NULL COMMENT '生日',\n" +
                "  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',\n" +
                "  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',\n" +
                "  `test` int(10) unsigned NOT NULL DEFAULT '1',\n" +
                "  PRIMARY KEY (`uid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        // 使用旧表创建新表
        String sql1_2 = "create table tab_new like tab_old";
        String sql1_3 = "create table tab_new as select col1,col2 from tab_old definition only";


        /* 插入数据 */
        // 插入单行数据
        String sql2_1 = "INSERT INTO user (name, age) VALUES ('Jim', 18);";
        // 插入多行数据
        String sql2_2 = "INSERT INTO user (name, age) VALUES ('Jim', 18),('Tom',19),('Andy',17);";

        /* 查询数据 */
        String sql3_1 = "select * from staffs";// √
        String sql3_2 = "SELECT uid id, name, salary FROM staffs WHERE salary > 11000;"; // √
        String sql3_3 = "SELECT gender, COUNT(1) FROM staffs GROUP BY gender;";// √
        String sql3_4 = "select * from t1 a inner join t2 b on a.id=b.id where id=1 and a.name='superz'"; // √
        // 条件子查询不会参与解析
        String sql3_5 = "SELECT * FROM staffs WHERE salary = (SELECT MAX(salary) FROM staffs);";// √
        // Fixme:未解析出字段具体所属的表
        String sql3_6 = "SELECT a.aid, a.title, c.name FROM articles a, categories c WHERE a.cid = c.cid;";
        String sql3_7 = "SELECT aid, title, name FROM articles a, categories c WHERE a.cid = c.cid AND a.cid = 1;";
        String sql3_8 = "SELECT aid, title, name FROM articles a INNER JOIN categories c ON a.cid = c.cid;";
        String sql3_9 = "SELECT aid, title, name FROM articles a JOIN categories c ON a.cid = c.cid;";
        String sql3_10 = "SELECT aid, title, name FROM articles a JOIN categories c ON a.cid = c.cid WHERE a.cid = 1;";
        String sql3_11 = "SELECT aid, title, name FROM articles a JOIN categories c USING(cid);";
        String sql3_12 = "SELECT * FROM articles a LEFT JOIN categories c ON a.cid = c.cid;";
        String sql3_13 = "SELECT * FROM articles a RIGHT JOIN categories c ON a.cid = c.cid;";
        String sql3_14 = "SELECT * FROM articles a LEFT JOIN categories c ON a.cid = c.cid WHERE c.cid IS NOT NULL;";
        String sql3_15 = "SELECT * FROM articles a RIGHT JOIN categories c ON a.cid = c.cid WHERE a.cid IS NOT NULL;";
        String sql3_16 = "SELECT aid, title, name FROM articles NATURAL JOIN categories;";
        String sql3_17 = "select * from (select x,y,z from t1) a";// √
        String sql3_18 = "select * from Student RIGHT JOIN " +
                "(select T1.sid,class1,class2 from " +
                "(select SId, score as class1 from sc where sc.CId = '01') as t1, " +
                "(select SId, score as class2 from sc where sc.CId = '02') as t2 " +
                "where t1.SId = t2.SId AND t1.class1 > t2.class2 )r on Student.SId = r.SId";

        /* WITH...AS语句 */
        String sql4_1 = "WITH xm_gl AS (SELECT * FROM products WHERE pname IN('小米电视机', '格力空调')) SELECT avg( price ) FROM xm_gl;";
        String sql4_2 = "WITH a AS ( SELECT * FROM category WHERE cname = '家电'),\n" +
                "b AS ( SELECT * FROM products WHERE pname IN('小米电视机', '格力空调'))\n" +
                "SELECT * FROM a LEFT JOIN b ON a.cid = b.category_id;";

        /* 视图 */
        String sql5_1 = "CREATE VIEW v_staffs AS SELECT * FROM staffs;";

        String sql6_1 = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(MysqlSQLParserMain.class.getResource("/sql/GG12-GCSN.sql").getPath()))) {
            String str;
            while ((str = reader.readLine()) != null) {
                sql6_1 += str;
                sql6_1 += "\n";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // log.info("{}", sql6_1);

        // 解析 SQL 语句，每一个 SQLStatement 代表一条完整的 SQL 语句
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql6_1, DB_TYPE);
        for (SQLStatement sqlStatement : statementList) {
            // SQLStatementUsage.usage(sqlStatement);

            log.info("SQL 分析语句：{}", SQLUtils.toSQLString(sqlStatement));
            Object result = SelectStatementHandler.handle((SQLSelectStatement) sqlStatement);
            log.info("SQL 分析结果：{}", result);
        }
    }

    private static void SchemaStatVisitorUsage(SQLStatement sqlStatement) {
        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(DB_TYPE);
        sqlStatement.accept(visitor);

        System.out.println(visitor.getColumns());
        System.out.println(visitor.getTables());
    }
}
