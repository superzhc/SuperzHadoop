# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/20 11:36
# ***************************
from sqllineage.runner import LineageRunner

sql = "insert into db1.table11 select * from db2.table21 union select * from db2.table22;"
sql += "insert into db3.table3 select * from db1.table11 join db1.table12;"
result = LineageRunner(sql)
print(result)

# result.draw("mysql")
