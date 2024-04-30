# 公共表表达式（Common Table Expression、CTE）

> Common Table Expressions（CTE）被引入标准SQL，以简化各种不适合派生表的 SQL 查询。
> 
> CTE 是在 SQL Server 2005 中引入的，通用表表达式（CTE）是一个临时命名的结果集，用户可以在 SELECT、INSERT、UPDATE 或 DELETE 语句中引用。

**语法**

```sql
WITH expression_name [(column_name[,...n])] 
AS 
(CTE_query_definition)
```

可同时定义 1 个或多个 CTE，使用英文逗号纪念性分隔

```sql
WITH expression_name1 AS
    (CTE_query_definition),
expression_name2 AS
    (CTE_query_definition)
    ...
Main query
```

## Supports

1. MySQL8.0 推出对 CTE 的新功能
2. PostgreSQL8.4 版本的一个新特性