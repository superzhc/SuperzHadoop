package com.github.superzhc.sql.parser.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.util.JdbcConstants;
import com.github.superzhc.sql.parser.druid.usage.SQLStatementUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author superz
 * @create 2022/6/14 15:25
 **/
public class HiveSQLParserMain {
    private static final Logger log = LoggerFactory.getLogger(HiveSQLParserMain.class);

    private static final DbType DB_TYPE = JdbcConstants.HIVE;

    public static void main(String[] args) {
        String sql1_1 = "create table temp.t1 as select x,y from default.t1";
        String sql1_2 = "CREATE  TABLE IF NOT EXISTS  EXPOSURE (           \n" +
                "            session_id string  COMMENT  'session_id',          \n" +
                "            kv map<string,string>            \n" +
                ") \n" +
                "PARTITIONED BY (\n" +
                "          day string\n" +
                ");";

        String sql2_1 = "INSERT ALL\n" +
                "    WHEN ottl < 100000 THEN\n" +
                "        INTO small_orders\n" +
                "            VALUES(oid, ottl, sid, cid)\n" +
                "    WHEN ottl > 100000 and ottl < 200000 THEN\n" +
                "        INTO medium_orders\n" +
                "            VALUES(oid, ottl, sid, cid)\n" +
                "    WHEN ottl > 200000 THEN\n" +
                "        into large_orders\n" +
                "            VALUES(oid, ottl, sid, cid)\n" +
                "    WHEN ottl > 290000 THEN\n" +
                "        INTO special_orders\n" +
                "SELECT o.order_id oid, o.customer_id cid, o.order_total ottl,\n" +
                "o.sales_rep_id sid, c.credit_limit cl, c.cust_email cem\n" +
                "FROM orders o, customers c\n" +
                "WHERE o.customer_id = c.customer_id;";
        String sql2_2="with A as (select id,name,age from tab1 where id > 100 ) ,\n" +
                "C as (select id,name,max(age) from A group by A.id,A.name) ,\n" +
                "B as (select id,name,age from tabb2 where age > 28)\n" +
                "insert into tab3\n" +
                "   select C.id,concat(C.name,B.name) as name, B.age from B,C where C.id = B.id";

        String sql3_1 = "  SELECT a.deptno                  \"Department\", \n" +
                "         a.num_emp / b.total_count \"Employees\", \n" +
                "         a.sal_sum / b.total_sal   \"Salary\" \n" +
                "  FROM   (SELECT deptno, \n" +
                "                 Count()  num_emp, \n" +
                "                 SUM(sal) sal_sum \n" +
                "          FROM   scott.emp \n" +
                "          WHERE  city = 'NYC' \n" +
                "          GROUP  BY deptno) a, \n" +
                "         (SELECT Count()  total_count, \n" +
                "                 SUM(sal) total_sal \n" +
                "          FROM   scott.emp \n" +
                "          WHERE  city = 'NYC') b \n" +
                ";";
        String sql3_2 = "select * from t1 left join t2 left join t3 left join t4 on t1.id=t2.id and t1.id=t3.id and t1.id=t4.id";

        List<SQLStatement> statements = SQLUtils.parseStatements(sql2_2, DB_TYPE);
        for (SQLStatement statement : statements) {
            SQLStatementUsage.usage(statement);
        }
    }
}
