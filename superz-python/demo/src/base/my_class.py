# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/2 9:45
# ***************************
"""
Python 面向对象编程
"""


class MyClass:
    """
    自定义类
    """

    # 定义属性
    f1: str = None

    # 定义私有属性
    __f2 = False

    def __init__(self, f1):
        """
        构造函数
        """
        self.f1 = f1

    def func(self):
        """
        定义类方法
        :return:
        """
        return self.f1

    def __private_fuc(self):
        """
        私有方法
        :return:
        """
        pass


class BaseClass:
    def __init__(self):
        pass

    def to_string(self):
        pass


class DerivedClass(BaseClass):
    """
    继承基类BaseClass
    """

    def __init__(self):
        pass

    def to_string(self):
        """
        重写父类的方法
        :return:
        """
        pass


class MultiDerivedClass(BaseClass, MyClass):
    """
    多继承

    需要注意圆括号中父类的顺序，若是父类中有相同的方法名，而在子类使用时未指定，python从左至右搜索 即方法在子类中未找到时，从左到右查找父类中是否包含方法。
    """

    def __init__(self):
        pass


if __name__ == "__main__":
    # 实例化类
    clazz = MyClass()
