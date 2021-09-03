#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: demo1
# Author: superz
# CreateTime: 2021/7/12 9:47
# Description:
# --------------------------------------
from sqlalchemy import create_engine, MetaData, Table, Column, ForeignKey
from sqlalchemy import Integer, String

"""
1. 创建一个连接引擎

create_engine("数据库类型+数据库驱动://数据库用户名:数据库密码@IP地址:端口/数据库"，其他参数)
"""
engine = create_engine("mysql+pymysql://root:123456@localhost:3306/superz", echo=True)

"""
2. 创建元数据

通过元数据绑定连接引擎，这样就可以当用户对这个元数据实例进行操作的时候直接连接到数据库
"""
metadata = MetaData(engine)

"""
3.1. 添加表结构
"""
user = Table("user", metadata,
             Column("id", Integer, primary_key=True),
             Column("name", String(200)),
             Column("age", Integer),
             Column("sex", String(2))
             )
address = Table('address', metadata,
                Column('id', Integer, primary_key=True),
                Column('user_id', None, ForeignKey('user.id')),
                Column('email', String(128), nullable=False)
                )
# 执行创建
metadata.create_all()
