# FAQ

## 打印执行语句

**方式一**

对于查询对象、插入更新对象，可直接通过 `str(query)` 打印出 SQL 语句，但需要注意的是，此种方式是不包含参数的

**方式二**

打印包含参数的执行语句，但参数只包括数字和字符串等基本类型；需要设置数据库方言，如 MySQL：

```py
from sqlalchemy.dialects import mysql
str(query.statement.compile(dialect=mysql.dialect(), compile_kwargs={"literal_binds": True}))
```