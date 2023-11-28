# DDL

## 数据库

### 创建数据库

```sql
-- 使用默认模板创建新数据库
CREATE DATABASE <database_name>;

-- 基于某个模板来创建新数据库
CREATE DATABASE <database_name> TEMPLATE <template_database_name>;
```

> 模板数据库
>
> 模板数据库就是创建新 database 时所使用的骨架。创建新 database 时，PostgreSQL 会基于模板数据库制作一份副本，其中会包含所有的数据库设置和数据文件。
>
> PostgreSQL 安装好以后默认附带两个模板数据库：`template0` 和 `template1`。如果创建新库时未指定使用哪个模板，那么系统默认会使用 `template1` 库作为新库的模板。

## Schema

### 创建 Schema

```sql
CREATE SCHEMA <schema_name>;
```

## 表