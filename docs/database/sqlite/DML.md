# DML

## 插入数据

**语法**

```sql
INSERT INTO TABLE_NAME [(column1, column2, column3,...columnN)]  
VALUES (value1, value2, value3,...valueN);
```

**`INSERT...SELECT`**

```sql
INSERT INTO first_table_name [(column1, column2, ... columnN)] 
   SELECT column1, column2, ...columnN 
   FROM second_table_name
   [WHERE condition];
```

**`INSERT OR REPLACE`**

> 如果不存在就插入，存在就更新，注：*只对主键、UNIQUE约束的字段起作用*

```sql
INSERT OR REPLACE INTO TABLE_NAME [(column1, column2, column3,...columnN)]  
VALUES (value1, value2, value3,...valueN);
```

**`INSERT OR IGNORE`**

> 如果不存在就插入，存在就忽略，注：*只对主键、UNIQUE约束的字段起作用*

```sql
INSERT OR IGNORE INTO TABLE_NAME [(column1, column2, column3,...columnN)]  
VALUES (value1, value2, value3,...valueN);
```

## 更新数据

**语法**

```sql
UPDATE table_name
SET column1 = value1, column2 = value2...., columnN = valueN
WHERE [condition];
```

## 删除数据

**语法**

```sql
DELETE FROM table_name
WHERE [condition];
```

## UPSERT 操作

### 使用 INSERT 语句的 ON CONFLICT 子句

```sql
-- 如果 key_column 发生冲突，则执行更新操作，可只更新部分列
INSERT INTO tableName [(column1, column2, ..., columnN)]
VALUES (value1, value2, ..., valueN)
ON CONFLICT(key_column) DO UPDATE SET [column1=value1[,column2=value2[, ..., columnN=valueN]]];
```

### 使用 REPLACE 语句

REPLACE 语句首先尝试插入新行，如果发生冲突，则会删除旧行，并插入新行。

```sql
REPLACE INTO tableName [(column1, column2, ..., columnN)]
VALUES (value1, value2, ..., valueN)
```