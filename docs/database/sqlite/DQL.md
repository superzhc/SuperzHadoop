# DQL

## 分页

**语法**

```sql
SELECT column1, column2, columnN 
FROM table_name
LIMIT [no of rows]

-- 设置偏移量
SELECT column1, column2, columnN 
FROM table_name
LIMIT [no of rows] OFFSET [row num]
```