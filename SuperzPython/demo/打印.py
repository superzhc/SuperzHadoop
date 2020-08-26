# coding=utf-8
'''
@Description: print的使用
@Author: superz
@Date: 2020-01-02 22:58:38
@LastEditTime : 2020-01-03 14:47:44
'''

a, b = 5, 10

# 格式化输出
print("%d * %d = %d" % (a, b, a*b))
print("{0} * {1} = {2}".format(a, b, a*b))
# Python 3.6以后，格式化字符串还有更为简洁的书写方式，就是在字符串前加上字母f
print(f"{a} * {b} = {a*b}")

lst = [1, 3, 5]

print(lst)

print(f"lst:{lst}")

print("lst:{}".format(lst))

print("lst:", lst)
