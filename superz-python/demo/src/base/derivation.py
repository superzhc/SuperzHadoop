# -*- coding: utf-8 -*-
# Python 推导式
# Python 推导式是一种独特的数据处理方式，可以从一个数据序列构建另一个新的数据序列的结构体。
# Python 支持各种数据结构的推导式：
# - 列表(list)推导式
# - 字典(dict)推导式
# - 集合(set)推导式
# - 元组(tuple)推导式
# @author: superz
# @create: 2023/6/2 0:20
# *****************************

def demo_list() -> None:
    """
    列表推导式

    [out_exp_res for out_exp in input_list]
    或
    [out_exp_res for out_exp in input_list if condition]
    :return:
    """
    names = ['Bob', 'Tom', 'alice', 'Jerry', 'Wendy', 'Smith']
    new_names = [name.upper() for name in names if len(name) > 3]
    print(new_names)


def demo_dict() -> None:
    """
    字典推导式

    { key_expr: value_expr for value in collection }
    或
    { key_expr: value_expr for value in collection if condition }
    :return:
    """
    dic = {x: x ** 2 for x in (2, 4, 6)}
    print(dic)


def demo_set() -> None:
    """
    集合推导式

    { expression for item in Sequence }
    或
    { expression for item in Sequence if conditional }
    :return:
    """
    pass


def demo_tuple():
    """
    元组推导式

    (expression for item in Sequence )
    或
    (expression for item in Sequence if conditional )
    :return:
    """
    pass
