/* 索引 */
/* 索引是存储引擎用于快速查找到记录的一种数据结构 */


/* 创建索引 */
/*
创建表时创建索引
CREATE TABLE <table_name>(
    <column_name> <data_type>...

    -- 创建索引
    [UNIQUE|FULLTEXT|SPATIAL] [INDEX|KEY] [index_name](column_name [length]...) [ASC|DESC]
)

UNIQUE|FULLTEXT|SPATIAL 为可选参数，分别表示唯一索引、全文索引和空间索引
INDEX|KEY 为同义词，两者作用相同，用来指定创建索引
index_name 指定索引的名称，为可选参数，如果不指定，MySQL 默认 column_name 为索引名称
column_name 为需要创建索引的字段列，该列必须从数据表中定义的多个列中选择
length 为可选参数，表示索引的长度，只有字符串类型的字段才能指定索引长度
ASC|DESC 指定升序或者降序的索引值存储
*/

/*
为已存在的表创建索引
1. 使用 ALTER TABLE 语句创建索引
ALTER TABLE <table_name> ADD [UNIQUE|FULLTEXT|SPATIAL] [INDEX|KEY] [index_name](column_name [length]...) [ASC|DESC]
2. 使用 CREATE INDEX 创建索引
CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name ON table_name(column_name [length]...) [ASC|DESC]
*/

/* 删除索引 */
/*
MySQL 中删除索引使用 ALTER TABLE 或者 DROP INDEX 语句。
1. 使用 ALTER TABLE 删除索引
ALTER TABLE table_name DROP INDEX index_name
2. 使用 DROP INDEX 删除索引
DROP INDEX index_name ON table_name
*/