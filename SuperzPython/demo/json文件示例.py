# coding=utf-8
'''
@Description: JSON文件处理示例
@Author: superz
@Date: 2020-01-04 00:17:12
@LastEditTime : 2020-01-04 00:33:35
'''

import json
from datetime import date

"""
json模块主要有四个比较重要的函数，分别是：
1.dump - 将Python对象按照JSON格式序列化到文件中
2.dumps - 将Python对象处理成JSON格式的字符串
3.load - 将文件中的JSON数据反序列化成对象
4.loads - 将字符串的内容反序列化成Python对象
"""

with open("./data/test.json", encoding="utf-8") as f:
    data_model = json.loads(f.read())
    print(type(data_model))
    print(data_model)

n = date.today()
d = {'name': '骆昊', 'age': 38, 'qq': 957658, 'friends': ['王大锤', '白元芳'], 'cars': [
    {'brand': 'BYD', 'max_speed': 180}, {'brand': 'Audi', 'max_speed': 280}, {'brand': 'Benz', 'max_speed': 320}]}
with open("./data/20200104.json", "w") as fw:
    json.dump(d, fw)
