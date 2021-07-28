package com.github.superzhc.xxljob.handler.param;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.github.superzhc.xxljob.util.CommandLineUtils;

/**
 * @author superz
 * @create 2021/7/26 17:54
 */
public class SQLParam {
    @Parameter(names = {"-url"},description = "连接信息")
    public String url;

    @Parameter(names = {"-u","--username"},description = "用户名")
    public String username;

    @Parameter(names = {"-p","--password"},description = "密码")
    public String password;

    @Parameter(names = {"-s","--sql"},description = "查询语句")
    public String sql;

    public static void main(String[] args) {
        String param="-url jdbc://namenode:3306/test --username root -p 123456 -s \"select * from test\"";

        SQLParam sqlParam=new SQLParam();
        JCommander.newBuilder().addObject(sqlParam).build().parse(CommandLineUtils.translateCommandline(param));
        System.out.println(sqlParam.url);
        System.out.println(sqlParam.username);
        System.out.println(sqlParam.password);
        System.out.println(sqlParam.sql);
    }
}
