# Engine

Engine 是 SQLAlchemy 应用程序的起点。

![](images/Engine20231212165736.png)

**创建 Engine**

```py
from sqlalchemy import create_engine
engine = create_engine('postgresql://scott:tiger@localhost:5432/mydatabase')
```

## 数据库 URL

创建 Engine 的 URL 的通用形式为：

```py
dialect+driver://username:password@host:port/database
```

