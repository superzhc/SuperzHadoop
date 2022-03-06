package com.github.superzhc.mysql.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mysql根据.frm和.ibd文件恢复表结构和数据
 *
 * @author Administrator
 */
public class MysqlDataRecover {
    private static final Logger log = LoggerFactory.getLogger(MysqlDataRecover.class);

    private final static String databaseName = "home_dw";

    /**
     * 1、先创建对应的表，至于具体有多少个列及各个列的消息消息先不用管，同一创建为包含列a的表。
     *
     * @throws Exception
     */
    public static void firstCreateTempTable() throws Exception {
        List<String> tables = getAllTables();
        for (String table : tables) {
            String c = "create table " + table;
            c += "( a int );";
            System.out.println(c);
        }
    }

    /**
     * 2、创建完成后，将数据库关闭，将所有的.frm文件移动备份都某个文件夹中去。
     *    将你想要还原的表中的所有.frm文件移动到此数据库中来。
     *    在my.ini或者my.cnf文件中添加如下配置： innodb_force_recovery = 6
     *    然后重启数据库
     */

    /**
     * 3、分析错误日志文件
     * 错误日志文件一般会有如下的错误日志输出：
     * [Warning] InnoDB: table marry/rc_marry contains 1 user defined columns in InnoDB, but 88 columns in MySQL. Please check INFORMATION_SCHEMA.INNODB_SYS_COLUMNS and http://dev.mysql.com/doc/refman/5.6/en/innodb-troubleshooting.html for how to resolve it
     * 具体信息解释为：  数据库下面的rc_marry表定义只有1个列（见第一步），然而在.frm中有88个列。
     * 从这里我们可以得到信息是：  rc_marry表有88个列。
     * 故我们通过这个错误信息又去创建相同表，而且此表有88个列，具体各个列的信息依旧不用关心。列数匹配即可。
     * 这里输出的sql是先删除，在创建表。
     * 需要将第二步骤的备份文件恢复回来，否则将会导致无法执行删除操作。
     * 在my.ini或者my.cnf文件中注释如下配置： #innodb_force_recovery = 6
     *
     * @throws Exception
     */
    public static void errorLogParseCreateTables() throws Exception {

        Map<String, Integer> tables = getTableColumnsNum();
        for (Entry<String, Integer> table : tables.entrySet()) {

            String c = "drop table if exists " + table.getKey() + ";\n";
            c += "create table " + table.getKey();
            c += "( ";
            for (int i = 0; i < table.getValue(); i++) {
                c += "a" + i + " int,";
            }
            c = c.substring(0, c.length() - 1);
            c += "  );";
            System.out.println(c);
        }
    }

    /**
     * 4、经过上面三个步骤，我们可以通过navcat工具将完整的表结构信息导出来，或者通过sql语句。
     * 然后在重新执行导出的语句，到此为止就算是将数据库结构ddl表结构恢复了。
     */
    public static void showCreateTables() throws Exception {

        //通过navcat自己导出表结构即可。
    }

    /**
     * 5、解除所有表的.ibd文件与.frm文件绑定
     */
    //.ibd文件与原先的.frm文件解除绑定
    public static void discardTablespace() throws Exception {

        List<String> tables = getAllTables();
        for (String table : tables) {
            String c = "alter table " + table + " discard tablespace;";
            System.out.println(c);
        }

    }

    /**
     * 6、将需要恢复的表的.ibd文件拷贝到数据库文件中，
     * 然后在重新执行绑定即恢复了数据
     */
    //.ibd文件与原先的.frm文件解除绑定
    public static void importTablespace() throws Exception {

        List<String> tables = getAllTables();
        for (String table : tables) {
            String c = "alter table " + table + " import  tablespace;";
            System.out.println(c);
        }

    }


    /**
     * 提取表名称和表具体字段数量的正则表达式
     */
    final static String getTableNameAndColumnsNum = ".*table " + databaseName + "/(\\S+) contains 1 user defined columns in InnoDB, but (\\d+) columns in MySQL. Please check INFORMATION_SCHEMA.INNODB_SYS_COLUMNS.*";

    /**
     * 通过具体的错误日志提取表名称及表的列数量
     *
     * @return
     * @throws Exception
     */
    private static Map<String, Integer> getTableColumnsNum() throws Exception {

        List<String> tables = getAllTables();
        BufferedReader reader = new BufferedReader(new FileReader("F:\\pdy\\path\\pdyerror.log"));
        String line = null;
        Map<String, Integer> result = new HashMap<>();
        try {
            while ((line = reader.readLine()) != null) {

                if (line.matches(getTableNameAndColumnsNum)) {
                    Matcher m = Pattern.compile(getTableNameAndColumnsNum).matcher(line);
                    while (m.find()) {
                        String table = m.group(1);
                        String cloumns = m.group(2);
                        if (tables.contains(table)) {
                            result.put(table, Integer.parseInt(cloumns));
                        }
                    }
                }
            }
        } finally {
            reader.close();
        }
        return result;

    }

    private static List<String> getAllTables() throws Exception {

//        BufferedReader reader = new BufferedReader(new FileReader("F:\\pdy\\path\\ta.txt"));
//        String line = null;
//
//        List<String> frm = new ArrayList<String>();
//        try {
//            while ((line = reader.readLine()) != null) {
//
//                String[] ss = line.split("\\s+");
//                for (String s : ss) {
//                    if (s.endsWith(".frm")) {
//                        frm.add(s.substring(0, s.length() - 4));
//                    }
//                }
//            }
//        } finally {
//            reader.close();
//        }

        List<String> frms = new ArrayList<String>();
        frms.add("data_railway");
        return frms;
    }

    private static List<String> getAllTables(String path) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;

        List<String> frm = new ArrayList<String>();
        try {
            while ((line = reader.readLine()) != null) {

                String[] ss = line.split("\\s+");
                for (String s : ss) {
                    if (s.endsWith(".frm")) {
//						System.out.println(s);
                        frm.add(s.substring(0, s.length() - 4));
                    }
                }
            }
        } finally {
            reader.close();
        }
        return frm;
    }

    public static void main(String[] args) throws Exception {
        List<String> s = getAllTables();
        List<String> ss = getAllTables("F:\\pdy\\path\\pdy.log");
        for (String table : ss) {
            if (!s.contains(table)) {
                System.out.println("drop talbe " + table + ";");
            }
        }
//		firstCreateTempTable();
//		errorLogParseCreateTables();

//		discardTablespace();
//		importTablespace();
    }
}