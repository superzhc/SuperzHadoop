# coding=utf-8
'''
@Description: Python中的集合跟数学上的集合是一致的，不允许有重复元素，而且可以进行交集、并集、差集等运算
@Author: superz
@Date: 2020-01-03 15:35:11
@LastEditTime : 2020-01-03 15:51:06
'''

# 创建集合
set1 = {1, 2, 3, 3, 3, 2}
print(set1)

# 集合中元素的个数
print(len(set1))

# 通过其他对象实例创建集合
lst = [1, 2, 3, 3, 3, 2]
set2 = set(lst)
print(set2)
set3 = set(range(1, 10))
print(set3)
set4 = set((1, 2, 3, 3, 3, 2))
print(set4)

# 创建集合的推导式语法（推导式也可以用于推导集合）
set5 = {num for num in range(1, 100) if num % 3 == 0 or num % 5 == 0}
print(set5)

"""添加和删除元素"""
set1.add(4)
print(set1)

set1.update([11, 12])
print(set1)

set3.discard(5)
if 4 in set3:
    set3.remove(4)
print(set3)

set4.pop()
print(set4)


# 集合的交集、并集、差集、对称差运算
print(set1 & set2)
# print(set1.intersection(set2))
print(set1 | set2)
# print(set1.union(set2))
print(set1 - set2)
# print(set1.difference(set2))
print(set1 ^ set2)
# print(set1.symmetric_difference(set2))
# 判断子集和超集
print(set2 <= set1)
# print(set2.issubset(set1))
print(set3 <= set1)
# print(set3.issubset(set1))
print(set1 >= set2)
# print(set1.issuperset(set2))
print(set1 >= set3)
# print(set1.issuperset(set3))