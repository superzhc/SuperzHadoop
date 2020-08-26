#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: StackDemo
# Author: superz
# CreateTime: 2020/8/25 23:49
# --------------------------------------

"""
使用列表来实现栈
"""

if __name__ == "__main__":
    stack = [3, 4, 5]
    stack.append(6)
    stack.append(7)
    print(stack)

    print(stack.pop())
    print(stack)
    print(stack.pop())
    print(stack.pop())
    print(stack)
