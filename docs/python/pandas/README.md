# pandas

## 安装

```sh
pip install pandas
```

**验证**

```py
import pandas as pd
print(pd.__version__)
```

## 数据结构

### Series

> Pandas Series 类似表格中的一个列（column），类似于一维数组，可以保存任何数据类型。

Series 由索引（index）和列组成，函数如下：

```py
pandas.Series(data, index, dtype, name, copy)
```

参数说明：

- data：一组数据(ndarray 类型)
- index：数据索引标签，如果不指定，默认从 0 开始
- dtype：数据类型，默认会自己判断
- name：设置名称
- copy：拷贝数据，默认为 False

### DataFrame

> DataFrame 是一个表格型的数据结构，它含有一组有序的列，每列可以是不同的值类型（数值、字符串、布尔型值）。DataFrame 既有行索引也有列索引，它可以被看做由 Series 组成的字典（共同用一个索引）。

DataFrame 构造方法如下：

```py
pandas.DataFrame(data, index, columns, dtype, copy)
```

参数说明：

- data：一组数据(ndarray、series, map, lists, dict 等类型)
- index：索引值，或者可以称为行标签
- columns：列标签，默认为 `RangeIndex(0, 1, 2, …, n)`
- dtype：数据类型
- copy：拷贝数据，默认为 False

**data 格式**

```py
# 二维数组
data1 = [['Google',10],['Runoob',12],['Wiki',13]]
df = pd.DataFrame(data,columns=['Site','Age'],dtype=float)

# ndarrays
data2 = {'Site':['Google', 'Runoob', 'Wiki'], 'Age':[10, 12, 13]}
df = pd.DataFrame(data)

# 字典
data3 = [{'a': 1, 'b': 2},{'a': 5, 'b': 10, 'c': 20}]
df = pd.DataFrame(data)
```