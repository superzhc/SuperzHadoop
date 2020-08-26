# coding=utf-8
'''
@Description: 文件示例
@Author: superz
@Date: 2020-01-03 23:46:21
@LastEditTime : 2020-01-04 00:15:36
'''

from demoutil import dividing_line as dl, is_prime

"""读取数据，模式为r（默认也是使用此模式的）"""
# 一次性读取整个文件内容
with open("./data/test.txt", "r", encoding="utf-8") as f:
    print(f.read())

dl()

# 通过for-in逐行读取
with open("./data/test.txt", "r", encoding="utf-8") as f:
    for line in f:
        # 去掉每行后面的换行符\n
        print(line[:-1] if line.endswith("\n") else line, end=" ")
print(" ")

dl()

# 读取文件按行读取到列表中
with open("./data/test.txt", "r", encoding="utf-8") as f:
    lines = f.readlines()
print(lines)

"""向文件写入数据"""
# with open("./data/test2.txt", "w") as f:
#     for number in range(1, 10000):
#         if is_prime(number):
#             f.write(str(number)+"\n")

"""读写二进制文件"""
with open('./data/pic1.png', 'rb') as fs1:
    data = fs1.read()
    print(type(data))
with open('./data/pic2.png', 'wb') as fs2:
    fs2.write(data)
