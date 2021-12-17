/* region 修改存储引擎 */

-- 方法一:alert table，该方法可以适用任何存储引擎。但存在一个问题，需要执行很长时间，MySQL 会按行将数据从原表复制到一张新的表中，在复制期间会消耗系统的 I/O 能力，同时原表上会加上读锁
alter table mdb_suzhounanjing engine = innodb;

-- 方法二：通过 mysqldump 工具将数据导出到文件，然后修改文件中 CREATE TABLE 语句的存储引擎选项，再将数据导入即可

-- 方法三：创建新表，通过 INSERT...SELECT... 语法来导入数据

/* endregion 修改存储引擎 */

