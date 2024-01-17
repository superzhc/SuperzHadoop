# Session

> Session 用于创建程序和数据库之间的会话，所有对象的载入和保存都需通过 Session 对象。
>
> 它可以管理建立连接中 engine，并为通过会话加载或与会话关联的对象提供标识映射 (identity map)。
>
> 在使用时与 Connection 非常相似

**创建会话**

*方式一*

```py
from sqlalchemy.orm import sessionmaker
Session = sessionmaker(bind=engine)
```

*方式二*

```py
from sqlalchemy.orm import sessionmaker
Session = sessionmaker()
Session.configure(bind=engine)
```

当使用时实例化对象调用

```py
session = Session()
```

**操作**

- `begin`：开始事务
- `flush`：预提交，提交到数据库文件，还未写入数据库文件中
- `commit`：提交了一个事务，把内存的数据直接写入数据库
- `rollback`：回滚
- `close`：关闭

**常用方法**

| 方法      | 参数                                                        | 描述                                       |
| --------- | ----------------------------------------------------------- | ------------------------------------------ |
| `add`     | `instance`                                                  | 下次刷新操作时，将 instance 保留到数据库中 |
| `delete`  | `instance`                                                  | 下次刷新操作时，将 instance 从数据库中删除 |
| `execute` | `statement` `params` `execution_option` `bind_arguments` 等 | 执行 SQL 表达式构造                        |
| `query`   | `*entities` `**kwargs`                                      | 返回 Query 对象, 可用于查询数据            |
| `refresh` | `instance` `attribute_names` `with_for_update`              | 为 instance 执行刷新操作                   |

## Query 对象

`query` 的返回对象为 `sqlalchemy.orm.query.Query` 对象，主要有以下的方法:

- `all()`：返回由表对象组成的列表
- `first()`：返回第一个结果 (表对象)，内部执行 `limit SQL`
- `one()`：只返回一行数据或引发异常 (无数据时抛出: `sqlalchemy.exc.NoResultFound`，多行数据时抛出: `sqlalchemy.exc.MultipleResultsFound`)
- `one_or_none()`：最多返回一行数据或引发异常 (无数据时返回 None，多行数据时抛出: `sqlalchemy.exc.MultipleResultsFound`)
- `scalar()`：获取第一行的第一列数据。如果没有要获取的行，则返回 None，多行数据时抛出: `sqlalchemy.exc.MultipleResultsFound`