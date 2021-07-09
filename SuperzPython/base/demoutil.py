# coding=utf-8
'''
@Description: 示例使用的工具类
@Author: superz
@Date: 2020-01-03 23:18:52
@LastEditTime : 2020-01-04 00:03:59
'''

from math import sqrt


def dividing_line(info="华丽的分割线", length=100, separate="-"):
    """
    生成分割线
    """
    print(info.center(length, separate))


def is_prime(n):
    """判断素数的函数"""
    for factor in range(2, int(sqrt(n))+1):
        if n % factor == 0:
            return False
    return True if n != 1 else True
