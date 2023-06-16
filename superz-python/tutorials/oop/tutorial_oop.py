# -*- coding: utf-8 -*-
# @author: superz
# @create: 2023/6/16 11:13
# ***************************
class A(object):
    custom_version=1.0

    def __init__(self):
        pass


if __name__=="__main__":
    instance=A()
    print(instance.__dir__())