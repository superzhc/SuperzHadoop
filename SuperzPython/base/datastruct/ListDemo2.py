#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: ListDemo2
# Author: superz
# CreateTime: 2020/8/25 23:58
# --------------------------------------

"""
列表推导式
"""

if __name__ == "__main__":
    squares = list(map(lambda x: x ** 2, range(10)))
    print(squares)

    # 列表推导式的使用
    squares2 = [x ** 2 for x in range(10)]
    print(squares2)

    # 复杂写法的列表推导式
    lst = [(x, y) for x in [1, 2, 3] for y in [3, 2, 1] if x != y]
    print(lst)
