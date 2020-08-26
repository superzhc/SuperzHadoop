# coding=utf-8
'''
@Description: 动态获取对象属性示例
@Author: superz
@Date: 2020-01-02 22:25:15
@LastEditTime : 2020-01-02 22:28:22
'''

from Common import Student

xiaoming = Student("001", "xiaoming")
name = getattr(xiaoming, "name")
print("姓名："+name)
