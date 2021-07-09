#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: RangeDemo
# Author: superz
# CreateTime: 2020/8/25 23:07
# --------------------------------------

if __name__ == "__main__":
    """
    示例：range方法
    """
    print("----------------华丽的分割线------------------")

    for i in range(5):
        print(i)

    print("----------------华丽的分割线------------------")

    for i in range(5, 10):
        print(i)

    print("----------------华丽的分割线------------------")

    for i in range(0, 10, 3):
        print(i)

    print("----------------华丽的分割线------------------")

    for i in range(-10, -100, -30):
        print(i)

    print("----------------华丽的分割线------------------")

    print(range(10))

    print("----------------华丽的分割线------------------")

    # print(sum(range(4)))
    #
    # print("----------------华丽的分割线------------------")

    lst = list(range(4))
    print(lst)
