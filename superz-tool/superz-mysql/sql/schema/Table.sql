/* 数据表操作 */

/*
创建表的语法：

CREATE TABLE <table_name>(
    <column_primary_key> <type> PRIMARY KEY [默认值],
    <column_name1> <type> [列级别约束条件] [默认值],
    <column_name2> <type> [列级别约束条件] [默认值],
    ...
    [表级别约束条件]

);
列级别约束条件:
1. 定义主键，这种只支持某一列成为主键：<column_primary_key> <type> PRIMARY KEY [默认值]
2. 非空约束： <column_name1> <type> not null
3. 唯一性约束：<column_name1> <type> unique

默认值：
<column_name1> <type> DEFAULT [默认值]

表级别约束条件:
1. 定义主键，这种方式支持多个列联合成一个主键：[CONSTRAINT <约束名>] PRIMARY KEY([column_name1],[column_name2],...)
2. 定义唯一性:[CONSTRAINT <约束名>] QNIQUE([column_name1],[column_name2],...)

设置表的属性值自动增加：
可以通过为表主键添加AUTO_INCREMENT关键字来实现。默认的，在MySQL中AUTO_INCREMENT的初始值是1，每新增一条记录，字段值自动加1。一个表只能有一个字段使用AUTO_INCREMENT约束，且该字段必须为主键的一部分。AUTO_INCREMENT约束的字段可以是任何整数类型（TINYINT、SMALLIN、INT、BIGINT等）。
*/
create table superz_mysql_t1
(
    id     int auto_increment not null,
    xuehao varchar(255) unique,
    age    int                not null default 25,
    name   varchar(255),
    constraint pk primary key (id)
);

-- 查看表的基本结构
describe user_info;
/*
  结果：
| Field | Type | Null | Key | Default | Extra |
| :--- | :--- | :--- | :--- | :--- | :--- |
| id | int\(11\) | NO | PRI | NULL | auto\_increment |
| account | varchar\(255\) | YES |  | NULL |  |
| password | varchar\(255\) | YES |  | NULL |  |
| type | varchar\(255\) | YES |  | NULL |  |
 */

-- 查看表详细结构语句
show create table user_info;

/*
修改表

MySQL使用 ALTER TABLE 语句修改表
*/
-- 修改表名:ALTER TABLE <旧表名> RENAME [TO] <新表名>
alter table mdb_sh rename mdb_sh2;
-- 修改字段的数据类型:ALTER TABLE <表名> MODIFY <字段名> <字段类型>
alter table mdb_sh modify ziduan15 varchar(255);
-- 修改字段名:ALTER TABLE <表名> CHANGE <旧字段名> <新字段名> <新数据类型>
alter table mdb_sh change ziduan15 note varchar(255);
-- 添加字段：ALTER TABLE <表名> ADD <新字段名> <数据类型> [约束条件] [FIRST | AFTER 已存在字段名]
alter table mdb_sh add note2 varchar(255) not null default '' after ziduan15;
-- 删除字段:ALTER TABLE <表名> DROP <字段名>
alter table mdb_sh drop note2;
-- 修改字段的排列位置:ALTER TABLE <表名> MODIFY <字段名1> <字段类型> FIRST|AFTER <字段名2>
alter table mdb_sh modify ziduan15 varchar(255) after memo;
-- 更改表的存储引擎:ALTER TABLE <表名> ENGINE=<更改后的存储引擎名>
alter table mdb_sh engine = MyISAM;
-- 删除表的外键约束：ALTER TABLE <表名> DROP FOREIGN KEY <外键约束名>


/* 删除数据表：DROP TABLE [IF EXISTS] 表1,表2... */
drop table if exists superz_mysql_t1;

show variables like 'character_set_database';