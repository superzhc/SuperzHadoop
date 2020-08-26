# coding=utf-8
'''
@Description: 判断对象是否有这个属性
@Author: superz
@Date: 2020-01-02 22:29:23
@LastEditTime : 2020-01-02 22:33:56
'''

from Common import Student

xiaoming = Student(id="001", name="xiaoming")
print("判断对象是否包含name属性："+str(hasattr(xiaoming, "name")))
