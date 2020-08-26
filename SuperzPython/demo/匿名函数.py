# coding=utf-8
'''
@Description: 匿名函数，也称为lambda函数
@Author: superz
@Date: 2020-01-05 21:37:34
@LastEditTime : 2020-01-05 21:47:54
'''

if __name__ == "__main__":
    # f=lambda x: x*3
    # print(f(3))

    lst = [1, 2, 3, 4, 5]
    suqaredList = map(lambda x: x*x, lst)
    # print(suqaredList)
    print([x for x in suqaredList])

    lst2 = filter(lambda x: x % 2 == 0, lst)
    # print(lst2)
    print([x for x in lst2])

    # result = reduce(lambda x, y: x+y, lst)
    # print(result)
