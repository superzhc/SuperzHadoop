#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: ListDemo
# Author: superz
# CreateTime: 2020/8/25 23:38
# --------------------------------------

"""
列表示例
"""

if __name__ == "__main__":
    fruits = ['orange', 'apple', 'pear', 'banana', 'kiwi', 'apple', 'banana']
    print(fruits.count('apple'))
    print(fruits.count('tangerine'))
    print(fruits.index('banana'))
    print(fruits.index('banana', 4))

    fruits.reverse()
    print(fruits)

    fruits.append('grape')
    print(fruits)

    fruits.sort()
    print(fruits)

    print(fruits.pop())
