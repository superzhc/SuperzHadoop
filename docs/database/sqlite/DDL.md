# DDL

## 创建表

**语法**

```sql
CREATE TABLE database_name.table_name(
   column1 datatype  PRIMARY KEY,
   column2 datatype,
   column3 datatype,
   .....
   columnN datatype
);

CREATE TABLE database_name.table_name(
   column1 datatype,
   column2 datatype,
   column3 datatype,
   .....
   columnN datatype,
   PRIMARY KEY(one or more columns)
);
```

### 约束

**NOT NULL 约束**

确保某列不能有 NULL 值。

**DEFAULT 约束**

当某列没有指定值时，为该列提供默认值。

```sql
-- 时间默认值，默认的时间是以格林尼治标准时间为基准的，因此在中国使用的话会正好早8个小时
logtime TIMESTAMP default CURRENT_TIMESTAMP
-- 本地当前时间，使用如下
logtime TIMESTAMP default (datetime('now', 'localtime'))
```

**UNIQUE 约束**

确保某列中的所有值是不同的。

**PRIMARY Key 约束**

唯一标识数据库表中的各行/记录。

### 自增列

> SQLite 的 AUTOINCREMENT 是一个关键字，用于表中的字段值自动递增。

对于自增列只能写为如下写法：

```sql
CREATE TABLE demo(
    -- 写成 ID INT PRIMARY KEY NOT NULL AUTOINCREMENT,创建表会失败
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    column1 text
)
```

## 删除表

**语法**

```sql
DROP TABLE database_name.table_name;
```

## 修改表

### 重命名表名

**语法**

```sql
ALTER TABLE 旧表名 RENAME TO 新表名 
```

### 添加字段

**语法**

```sql
ALTER TABLE 表名 ADD COLUMN 列名 数据类型 
```

## 创建视图

**语法**

```sql
CREATE [TEMP | TEMPORARY] VIEW view_name AS SELECT...
```

## 删除视图

**语法**

```sql
DROP VIEW view_name;
```