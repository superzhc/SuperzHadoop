package com.github.superzhc.data.file;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2021/12/14 16:37
 */
public class MdbData implements FileData {

    private String path;
    private JdbcHelper jdbc;

    public MdbData(String path) {
        this.path = path;
        init();
    }

    private void init() {
        String url = String.format("jdbc:ucanaccess://%s;memory=false", path);
        jdbc = new JdbcHelper(url);
    }

    @Override
    public void preview(Integer number) {
        String[] tables = jdbc.tables();
        String sqlTemplate = "SELECT TOP %d * FROM %s";
        for (String table : tables) {
            String sql = String.format(sqlTemplate, number, table);
            System.out.println("Table:" + table);
            jdbc.show(sql);
            System.out.println("\n");
        }
    }

    public void count() {
        String[] tables = jdbc.tables();
        String sqlTemplate = "SELECT count(*) FROM %s";
        for (String table : tables) {
            String sql = String.format(sqlTemplate, table);
            long number = jdbc.aggregate(sql);
            System.out.println("Table[" + table + "] count:" + number);
        }
    }

    public void ddl() {
        String[] tables = jdbc.tables();
        for (String table : tables) {
            String[] columns = jdbc.columns(table);

            StringBuilder sb = new StringBuilder();
            for (String column : columns) {
                sb.append(",").append(column);
            }
            System.out.println("Table[" + table + "] columns:" + sb.substring(1));
        }
    }

    public static void main(String[] args) {
        String path;
        // path="D:\\downloads\\baidu\\car\\数据包一\\保险公司全国车主信息4.8G\\南京\\2010年南京车主名单.mdb";
        path = "D:\\downloads\\baidu\\car\\数据包一\\保险公司全国车主信息4.8G\\广东-顺德\\顺德车主（车管所数据）.mdb";
        MdbData mdbData = new MdbData(path);
        //mdbData.preview();
        //mdbData.count();
        mdbData.ddl();
    }
}
