# `Inspector`

> 一个底层接口，提供了获取数据库，表，列和约束描述等相关信息。

```py
from sqlalchemy import inspect, create_engine
engine = create_engine('...')
insp = inspect(engine)
```