# random 模块-随机数

## `numpy.random.seed(数字)`

设置 seed 的参数可以保证相同的参数下，重复去这个参数下的随机数会是相同的；如果不设置 seed，则每次会生成不同的随机数。

注意：每次使用 `numpy.random.rand...` 这类参数的时候，都需要在前面加上 seed 来设置一下，不然在用过一次取随机参数后，就会变为默认的随机种子。

## `numpy.random.RandomState`

`numpy.random.RandomState` 是一个伪随机数生成器。

> 伪随机数是用确定性的算法计算出来的似来自 $[0,1]$ 均匀分布的随机数序列。并不真正的随机，但具有类似于随机数的统计特征，如均匀性、独立性等。

### `RandomState.normal(loc=0.0, scale=1.0, size=None)`

从正态（高斯）分布中抽取随机样本。

- loc：float 或 array_like of floats
    分布的平均值(“centre”)。
- scale：float 或 array_like of floats
    分布的标准方差(“width”)。非负数
- size：int 或 int 型的元组，可选参数

## `numpy.random.random()`

随机生成的一个实数（浮点数），它在[0,1)范围内。

该方法只有一个参数 `size`，该参数有三种取值：

- None
- 整数类型
- 整数型元组

**示例**

```py
x1=np.random.random() #0.14775128911185142
x2=np.random.random((3,3)) # 注意参数是元组
```

## `numpy.random.rand(d0, d1, …, dn)`

产生 `[0,1)` 之间均匀分布的随机浮点数，其中 `d0,d1....` 表示传入的数组形状。

**示例**

```py
#产生形状为(2,)的数组，也就是相当于有2个元素的一维数组。
temp1=np.random.rand(2)

#产生一个2*4的数组，数组中的每个元素是[0,1)之间均匀分布的随机浮点数
temp2=np.random.rand(2,4)

#在rand()里面也可以没有参数，返回一个[0,1)之间的随机浮点数
#产生一个[0,1)之间的随机数
temp3=np.random.rand() #0.6143086490875544
```

## `numpy.random.randn(d0, d1, …, dn)`

从标准正态分布中返回一个或多个样本值。 参数表示样本的形状。所谓标准正态分布就是指这个函数产生的随机数，服从均值为 0，方差为 1 的分布，使用方法和 `rand()` 类似。

**指定数学期望和方差的正态分布**

实现方法为 `sigma * np.random.randn(...) + mu`，该公式可以实现一组随机数，服从均值为 mu，方差为 `sigma^2` 的正态分布

## `numpy.random.uniform(low=0.0, high=1.0, size=None)`

从指定范围内产生均匀分布的随机浮点数。

- low 表示范围的下限，float 型，或 float 型数组，默认为 `0.0`
- high 表示范围的上限，float 型，或 float 型数组，默认为 `1.0`
- size 表示“形状”或“个数”，int 型，或 int 型元组，默认为 None