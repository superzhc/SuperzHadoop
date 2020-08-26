# coding=utf-8
'''
@Description: 类/对象的使用
@Author: superz
@Date: 2020-01-02 22:42:39
@LastEditTime : 2020-01-02 22:56:12
'''

from Common import Student, undergraduate

xiaoming = Student("001", "xiaooming")

# 获取某个对象的类型
print(type(xiaoming))

# 判断object是否为类classinfo的实例，若是则返回true，否则返回false
print(isinstance(xiaoming, Student))

# 如果class是classinfo元组中某个元素的子类，也会返回True
print(issubclass(undergraduate, Student))
print(issubclass(Student, undergraduate))
print(issubclass(object, Student))
