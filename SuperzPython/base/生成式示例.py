# coding=utf-8
'''
@Description: 生成式示例
@Author: superz
@Date: 2020-01-04 22:32:56
@LastEditTime : 2020-01-04 22:47:44
'''

from demoutil import dividing_line as dl

""" 列表 """
lst = [x for x in range(1, 10)]
print(lst)

lst1 = [x for x in range(1, 10) if x % 2 == 0]
print(lst1)

dl()

""" 集合 """
s = {x % 5 for x in range(1, 10)}
print(s)

dl()

""" 字典 """
m = {f"key-{x}": f"value-{x}" for x in range(1, 10)}
print(m)

dl()
