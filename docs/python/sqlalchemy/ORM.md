# ORM

## 声明映射

> 声明性基类 `declarative_base`

```py
from sqlalchemy.ext.declarative import declarative_base
Base = declarative_base()
```

## 映射对应类

```py
class ObjectClass(Base):
    # 对应数据库关联表
    __tablename__ = 'table_name'
    # 字段, 数据类型，[主键自增]
    id = Column(Integer, primary_key=True)
    name = Column(String(50))
```

> 通过映射声明类的 table 属性查看关于映射表的信息，即 `ObjectClass.__table__`