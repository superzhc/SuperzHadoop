package com.github.superzhc.db.postgres;

import org.jdbi.v3.core.Jdbi;

public class PostgresMain {
    public static void main(String[] args) {
        String driver="org.postgresql.Driver";
        String url="jdbc:postgresql://127.0.0.1:5432/openmetadata_db";
        String username="postgres";
        String password="password";

        final Jdbi jdbi=Jdbi.create(url,username,password);
        jdbi.useHandle(handle -> {

        });
    }
}
