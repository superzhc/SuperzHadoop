# Engine

Engine 是 SQLAlchemy 应用程序的起点。

![](images/Engine20231212165736.png)

> Engine 内部维护了一个 Pool（连接池）和 Dialect（方言），方言来识别具体连接数据库种类。
>
> 创建好了 Engine，即同时已经创建完成 Pool 和 Dialect，但是此时并没有真正与数据库连接，等到执行具体的语句 `connect()` 时才会连接到数据库。

**创建 Engine**

```py
from sqlalchemy import create_engine
engine = create_engine('postgresql://scott:tiger@localhost:5432/mydatabase')
```

*常用参数*

- `echo=False` -- 如果为真，引擎将记录所有语句以及 `repr()` 其参数列表的默认日志处理程序
- `enable_from_linting` -- 默认为 `True`。如果发现给定的 SELECT 语句与将导致笛卡尔积的元素取消链接，则将发出警告
- `encoding` -- 默认为 `utf-8`
- `future` -- 使用 2.0 样式
- `hide_parameters` -- 布尔值，当设置为 True 时，SQL 语句参数将不会显示在信息日志中，也不会格式化为 StatementError 对象。
- `listeners` -- 一个或多个列表 PoolListener 将接收连接池事件的对象。
- `logging_name` -- 字符串标识符，默认为对象 id 的十六进制字符串。
- `max_identifier_length` -- 整数；重写方言确定的最大标识符长度。
- `max_overflow=10` -- 允许在连接池中“溢出”的连接数，即可以在池大小设置（默认为 5）之上或之外打开的连接数。
- `pool_size=5` -- 在连接池中保持打开的连接数
- `plugins` -- 要加载的插件名称的字符串列表。

## 数据库 URL

创建 Engine 的 URL 的通用形式为：

```py
dialect+driver://username:password@host:port/database
```

