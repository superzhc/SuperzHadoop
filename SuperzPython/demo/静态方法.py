# coding=utf-8
'''
@Description: 静态方法示例
@Author: superz
@Date: 2020-01-02 22:12:42
@LastEditTime : 2020-01-02 22:16:53
'''


class Student():
    def __init__(self, id, name):
        self.id = id
        self.name = name

    def __repr__(self):
        return "id="+self.id+",name="+self.name

    @classmethod
    def f(cls):
        '''
        classmethod 装饰器对应的函数不需要实例化，不需要 self 参数，但第一个参数需要是表示自身类的 cls 参数，可以来调用类的属性，类的方法，实例化对象等。
        '''
        print(cls)


Student.f()