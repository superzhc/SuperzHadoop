# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/27 9:33
# ***************************
"""
生成器
"""


def square():
    for x in range(4):
        yield x ** 2


square_gen = square()
for x in square_gen:
    print(x)
