# SQLite 内置表 `SQLITE_MASTER`

> SQLite 数据库中有一个内置表，名为 `SQLITE_MASTER`，此表中存储着当前数据库中所有表的相关信息，比如表的名称、用于创建此表的sql语句、索引、索引所属的表、创建索引的 sql 语句等。
>
> `SQLITE_MASTER` 表示只读的，只能对他进行读操作，写操作只能由系统自身触发，使用者没有权限。所有对用户自定义表的结构修改操作，会自定更新到此表。

`SQLITE_MASTER` 表的结构如下:

```sql
CREATE TABLE sqlite_master( 
    type TEXT, 
    name TEXT, 
    tbl_name TEXT, 
    rootpage INTEGER, 
    sql TEXT 
);
```

## 应用场景

**查询表信息**

> 如果要查询表的信息，则 type 字段为 `table`，name 字段为表的名称，返回结果中返回的 sql 字段，为创建此表的 sql 语句。

```sql
select * from sqlite_master where type='table' and name='表名';
```

**查询索引信息**

> 如果要查询索引信息，则 type 字段为 `index`，name 字段为索引名称，返回结果中的 `tbl_name` 字段为该索引所属的表，sql 字段为创建此索引的 sql 语句。

```sql
select * from sqlite_master where type='index' and name='索引名';
```

## 临时表

临时表不包含在 `SQLITE_MASTER` 表中，`SQLITE_TEMP_MASTER` 专门用来存储临时表的信息，此表和 `SQLITE_MASTER` 表的结构一致。