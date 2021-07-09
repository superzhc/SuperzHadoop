# coding=utf-8
'''
@Description: 列表相关示例
@Author: superz
@Date: 2020-01-03 14:50:22
@LastEditTime : 2020-01-03 15:08:05
'''

list1 = [1, 3, 5, 7, 100]
print(list1)

# 乘号表示列表元素的重复
list2 = ["hello"]*3
print(list2)

# 计算列表长度（元素个数）
print(len(list1))

# 下标（索引）获取元素
print(list1[0])
print(list1[4])
print(list1[-1])
print(list1[-5])
list1[2] = 300
print(list1)

# 判断元素是否存在
print(3 in list1)
print(1000 in list1)

# 通过循环下标来遍历元素
for index in range(len(list1)):
    print(list1[index])
# 通过循环遍历列表元素
for ele in list1:
    print(ele)
# 通过enumrate函数处理列表之后再遍历可以同时获得元素的索引和值
for index, ele in enumerate(list1):
    print(index, ele)

# 添加元素
list1.append(200)
list1.insert(1, 400)
print(list1)

# 合并两个列表
list1 += [1000, 2000]
print(list1)

# 删除元素
list1.remove(2000)
print(list1)

# 从指定位置删除元素
list1.pop(0)
print(list1)
list1.pop(len(list1)-1)
print(list1)

# 清空列表
list1.clear()
print(list1)

"""
列表切片操作
"""
fruits = ['grape', 'apple', 'strawberry', 'waxberry']
fruits += ['pitaya', 'pear', 'mango']
# 列表切片
fruits2 = fruits[1:4]
print(fruits2)
# 可以通过完整切片操作来复制列表
fruits3 = fruits[:]
print(fruits3)
fruits4 = fruits[-3:-1]
print(fruits4)
# 可以通过反向切片操作来获得倒转后的列表的拷贝
fruits5 = fruits[::-1]
print(fruits5)
