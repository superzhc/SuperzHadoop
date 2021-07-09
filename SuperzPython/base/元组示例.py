# coding=utf-8
'''
@Description: 元组示例
@Author: superz
@Date: 2020-01-03 15:26:06
@LastEditTime : 2020-01-03 15:33:13
'''

# 定义元组
t = ("superz", 27, True, "Epoint")
print(t)
# 获取元组中的元素
print(t[0])
print(t[3])
# 遍历元组中的值
for ele in t:
    print(ele)

# 重新给元组赋值
# t[0] = '王大锤'  # 报错：TypeError
# 变量t重新引用了新的元组原来的元组将被垃圾回收
# t = ('王大锤', 20, True, '云南昆明')

# 将元组转换成列表
person = list(t)
print(person)
# 修改列表中的元素
person[0] = "王大锤"
person[1] = 25
print(person)
# 将列表转换成元组
t2 = tuple(person)
print(t2)
