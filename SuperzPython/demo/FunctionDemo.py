#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# --------------------------------------
# FileName: FunctionDemo
# Author: superz
# CreateTime: 2020/8/25 23:19
# --------------------------------------

"""
函数示例
"""


def ask_ok(prompt, retries=4, reminder="请再次尝试"):
    while True:
        ok = input(prompt)
        if ok in ("y", "ye", "yes"):
            return True
        if ok in ("n", "no", "nop", "nope"):
            return False
        retries = retries - 1
        if retries < 0:
            raise ValueError("无效的响应")
        print(reminder)


def parrot(voltage, state='a stiff', action='voom', type='Norwegian Blue'):
    print("-- This parrot wouldn't", action, end=' ')
    print("if you put", voltage, "volts through it.")
    print("-- Lovely plumage, the", type)
    print("-- It's", state, "!")


def cheeseshop(kind, *arguments, **keywords):
    print("-- Do you have any", kind, "?")
    print("-- I'm sorry, we're all out of", kind)
    for arg in arguments:
        print(arg)
    print("-" * 40)
    for kw in keywords:
        print(kw, ":", keywords[kw])


if __name__ == "__main__":
    # ask_ok("Do you really want to quit?")
    # print("----------------华丽的分割线------------------")
    # ask_ok("OK to overwrite the file?", 2)
    # print("----------------华丽的分割线------------------")
    # ask_ok("OK to overwrite the file?", 2, "Come on, only yes or no!")
    # print("----------------华丽的分割线------------------")

    # parrot(1000)  # 1 positional argument
    # print("----------------华丽的分割线------------------")
    # parrot(voltage=1000)  # 1 keyword argument
    # print("----------------华丽的分割线------------------")
    # parrot(voltage=1000000, action='VOOOOOM')  # 2 keyword arguments
    # print("----------------华丽的分割线------------------")
    # parrot(action='VOOOOOM', voltage=1000000)  # 2 keyword arguments
    # print("----------------华丽的分割线------------------")
    # parrot('a million', 'bereft of life', 'jump')  # 3 positional arguments
    # print("----------------华丽的分割线------------------")
    # parrot('a thousand', state='pushing up the daisies')  # 1 positional, 1 keyword
    # print("----------------华丽的分割线------------------")

    cheeseshop("Limburger", "It's very runny, sir.",
               "It's really very, VERY runny, sir.",
               shopkeeper="Michael Palin",
               client="John Cleese",
               sketch="Cheese Shop Sketch")
