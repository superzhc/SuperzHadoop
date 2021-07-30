package com.github.superzhc.xxljob.util.db;

public enum Driver
{
    MySQL("com.mysql.jdbc.Driver"), Oracle("oracle.jdbc.driver.OracleDriver"), SQLServer(
            "com.microsoft.sqlserver.jdbc.SQLServerDriver"), SQLServer_v2(
                    "com.microsoft.jdbc.sqlserver.SQLServerDriver"), PostgreSQL("org.postgresql.Driver"), DB2(
                            "com.ibm.db2.jdbc.app.DB2.Driver"), Informix("com.informix.jdbc.IfxDriver"), Sysbase(
                                    "com.sybase.jdbc.SybDriver"), ODBC("sun.jdbc.odbc.JdbcOdbcDriver");

    private String fullClassName;

    private Driver(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String fullClassName() {
        return fullClassName;
    }

    public static Driver match(String url) {
        if (url.startsWith("jdbc:mysql:")) {
            return MySQL;
        }
        else if (url.startsWith("jdbc:microsoft:sqlserver:")) {
            return SQLServer;
        }
        else if (url.startsWith("jdbc:oracle:thin:")) {
            return Oracle;
        }
        else if (url.startsWith("jdbc:postgresql:")) {
            return PostgreSQL;
        }
        else if (url.startsWith("jdbc:db2:")) {
            return DB2;
        }
        else if (url.startsWith("jdbc:Informix-sqli:")) {
            return Informix;
        }
        else if (url.startsWith("jdbc:Sysbase:")) {
            return Sysbase;
        }
        else if (url.startsWith("jdbc:odbc:")) {
            return ODBC;
        }
        return null;
    }
}
