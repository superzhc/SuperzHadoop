# coding=utf-8
'''
@Description: 字典示例
@Author: superz
@Date: 2020-01-03 15:51:35
@LastEditTime : 2020-01-03 16:08:56
'''

"""创建字典"""
# 通过字面量创建字典
scores = {"superz": 90, "aa": 91, "bb": 89}
print(scores)

# 通过构造器语法创建字典
items = dict(one=1, two=2, three=3, four=4)
print(items)

# 通过zip函数将两个序列压成字典
items2 = dict(zip(["a", "b", "c"], "123"))
print(items2)

# 通过推导式语法创建字典
items3 = {num: num**2 for num in range(1, 10)}
print(items3)

"""操作字典"""
print(scores["superz"])

# 对字典中所有键值进行遍历
for key in scores:
    print(f"{key}:{scores[key]}")

# 更新字典中的元素
scores["superz"] = 70
scores["张三"] = 75
print(scores)
scores.update(cc=85, 张三=78)
print(scores)

if "李四" in scores:
    print(scores["李四"])

print(scores.get("superz"))

# 删除字典中的元素
print(scores.popitem())
print(scores.pop("superz", 88))

# 清空字典
scores.clear()
print(scores)
