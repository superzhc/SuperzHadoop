# coding=utf-8
'''
@Description: 字符串相关操作示例
@Author: superz
@Date: 2020-01-03 00:40:47
@LastEditTime : 2020-01-03 14:49:24
'''


def reverse_str1(str):
    '''
    反转字符串
    '''
    return ''.join(reversed(str))


def reverse_str2(str):
    '''
    反转字符串
    '''
    return str[::-1]


str = "superz"

print(reverse_str1("superz"))
print(reverse_str2("superz"))

# 获取一个字符串的长度
print(len(str))
# 首字母大写
print(str.capitalize())
# 每个单词首字母大写
print((str+" epoint").title())
# 字符串大写
print(str.upper())
# 从字符串中查找子串所在位置
print(str.find("er"))
print(str.find("hello"))
# 与find类似但找不到子串时会发生异常
# print(str.index("er"))
# print(str.index("hello"))
# 判断字符串是否以某个子串开头
print(str.startswith("su"))
print(str.startswith("SU"))
# 判断字符串是否以某个子串结尾
print(str.endswith("RZ"))
print(str.endswith("rz"))
# 将字符串以指定的宽度居中并在两侧填充指定的字符
print(str.center(50, '*'))
# 将字符串以指定的宽度靠右放置左侧填充指定的字符
print(str.rjust(50, ' '))
# 字符串转字节
print(bytes(str, encoding="utf8"))
