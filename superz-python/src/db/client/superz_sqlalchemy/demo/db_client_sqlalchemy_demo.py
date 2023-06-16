# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/15 16:50
# ***************************
from sqlalchemy import create_engine

# 创建数据库连接，典型的 database url 语法规则：`dialect+driver://username:password@host:port/database`
engine = create_engine("mysql+pymysql://root:123456@127.0.0.1:3306/xgit")

with engine.connect() as conn:
    result = conn.execute("select * from test_20230601_001")
    print(result.all())