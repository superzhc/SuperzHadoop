#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: IFDemo
# Author: superz
# CreateTime: 2020/8/25 23:01
# --------------------------------------

if __name__ == "__main__":
    x = int(input("请输入一个整数："))
    if x < 0:
        x = 0
        print("负数变为零")
    elif x == 0:
        print("零")
    elif x == 1:
        print("单数")
    else:
        print("复数")
