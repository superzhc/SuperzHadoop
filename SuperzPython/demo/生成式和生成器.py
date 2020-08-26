# coding=utf-8
'''
@Description: 生成式和生成器示例
@Author: superz
@Date: 2020-01-03 15:11:00
@LastEditTime : 2020-01-03 15:25:32
'''

import sys

f = [x for x in range(1, 10)]
print(f)
f = [x+y for x in "ABCDE" for y in "1234567"]
print(f)

# 用列表的生成表达式语法创建列表容器
# 用这种语法创建列表之后元素已经准备就绪所以需要耗费较多的内存空间
f = [x**2 for x in range(1, 100)]
print(sys.getsizeof(f))
print(f)
# 请注意下面的代码创建的不是一个列表而是一个生成器对象
# 通过生成器可以获取到数据但它不占用额外的空间存储数据
# 每次需要数据的时候就通过内部的运算得到数据(需要花费额外的时间)
f = (x ** 2 for x in range(1, 100))
print(sys.getsizeof(f))  # 相比生成式生成器不占用存储数据的空间
print(f)
for val in f:
    print(val)

# 除了上面提到的生成器语法，Python还有另外一种定义生成器的方式，就是通过yield关键字将一个普通函数改造成生成器函数


def fib(n):
    a, b = 0, 1
    for _ in range(n):
        a, b = b, a+b
        yield a


for val in fib(20):
    print(val)
