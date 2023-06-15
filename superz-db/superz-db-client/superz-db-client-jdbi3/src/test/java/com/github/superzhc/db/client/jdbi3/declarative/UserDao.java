package com.github.superzhc.db.client.jdbi3.declarative;

import org.jdbi.v3.core.mapper.MapMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Map;

public interface UserDao {
    @SqlUpdate("CREATE TABLE \"user\" (id INTEGER PRIMARY KEY, \"name\" VARCHAR)")
    void createTable();

    @SqlUpdate("INSERT INTO \"user\" (id, \"name\") VALUES (?, ?)")
    void insertPositional(int id, String name);

    @SqlUpdate("INSERT INTO \"user\" (id, \"name\") VALUES (:id, :name)")
    void insertNamed(@Bind("id") int id, @Bind("name") String name);

//    @SqlUpdate("INSERT INTO \"user\" (id, \"name\") VALUES (:id, :name)")
//    void insertBean(@BindBean User user);
//
//    @SqlQuery("SELECT * FROM \"user\" ORDER BY \"name\"")
//    @RegisterBeanMapper(User.class)
//    List<User> listUsers();

    @SqlQuery("SELECT * FROM date_dim limit 10")
    @RegisterRowMapper(MapMapper.class)
    List<Map<String, Object>> listData();
}