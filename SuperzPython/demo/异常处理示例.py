# coding=utf-8
'''
@Description: 异常处理示例
@Author: superz
@Date: 2020-01-03 23:07:27
@LastEditTime : 2020-01-03 23:36:13
'''

from demoutil import dividing_line as dl
import logging
import sys


def except_demo(a, b):
    """
    python异常处理结构如下：
    try:
        pass
    except expression as identifier:
        pass
    else:
        pass
    finally:
        pass

    except语句可以有多个，Python会按except语句的顺序依次匹配你指定的异常，如果异常已经处理就不会再进入后面的except语句
    """
    try:
        print(a/b)
    except ZeroDivisionError:
        print("Error:b should not be 0")
    except Exception as e:
        print(f"Unexcepted Error:{e}")
    else:
        print('Run into else only when everything goes well')
    finally:
        print('Always run into finally block.')


"""except语句不是必须的，finally语句也不是必须的，但是二者必须要有一个，否则就没有try的意义了"""


def mutiple_exception_in_one_line(a, b):
    """
    except语句可以以元组形式同时指定多个异常
    """
    try:
        print(a/b)
    except (ZeroDivisionError, TypeError) as e:
        print(f"异常信息：{e}")


def uncatch_exception(a, b):
    """
    1.except语句后面如果不指定异常类型，则默认捕获所有异常，可以通过sys模块获取当前异常
    2.如果要捕获异常后要重复抛出，请使用raise，后面不要带任何参数或信息
    """
    try:
        print(a/b)
    except:
        logging.exception("异常捕获！")

        error_type, error_value, trace_back = sys.exc_info()
        print(error_value)
        # raise


if __name__ == "__main__":
    except_demo(2, 0)
    dl()
    except_demo(2, "非数字进行相除")
    dl()
    except_demo(2, 1)
    dl()
    mutiple_exception_in_one_line(2, 0)
    dl()
    mutiple_exception_in_one_line(2, "非数字相除")
    dl()
    uncatch_exception(2, 0)
    dl()
    uncatch_exception(2, "非数字相除")
