#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: DequeDemo
# Author: superz
# CreateTime: 2020/8/25 23:51
# --------------------------------------

from collections import deque

"""
队列示例：
列表也可以用作队列，其中先添加的元素被最先取出 (“先进先出”)；然而列表用作这个目的相当低效。因为在列表的末尾添加和弹出元素非常快，但是在列表的开头插入或弹出元素却很慢 (因为所有的其他元素都必须移动一位)。
若要实现一个队列， collections.deque 被设计用于快速地从两端操作。
"""

if __name__ == "__main__":
    queue = deque(["Eric", "John", "Michael"])
    queue.append("Terry")
    queue.append("Graham")
    print(queue)
    print(queue.popleft())
    print(queue.popleft())
    print(queue)
