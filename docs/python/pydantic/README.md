# pydantic

pydantic 库是 python 中用于数据接口定义检查与设置管理的库。

pydantic 在运行时强制执行类型提示，并在数据无效时提供友好的错误。

## 安装

```sh
pip install pydantic
```

**测试是否已编译**

```py
import pydantic
print('compiled:', pydantic.compiled)
```

## 标准类型

| 类型                                    | 描述                                                                                      |
| --------------------------------------- | ----------------------------------------------------------------------------------------- |
| `None`，`type(None)` 或 `Literal[None]` | 只允许 None 值                                                                            |
| `int`                                   | 整数类型                                                                                  |
| `float`                                 | 浮点数类型                                                                                |
| `bool`                                  | 布尔类型                                                                                  |
| `str`                                   | 字符串类型                                                                                |
| `bytes`                                 | 字节类型                                                                                  |
| `object`                                | 任意对象（公共基类）                                                                      |
| **容器类型**                            | Python3.9 以前容器这种复合类型用简单的 list，dict，tuple 不能够明确说明内部元素的具体类型 |
| `list`                                  | 允许 list,tuple,set,frozenset,deque, 或生成器并转换为列表                                 |
| `tuple`                                 | 允许 list,tuple,set,frozenset,deque, 或生成器并转换为元组                                 |
| `dict`                                  | 字典类型                                                                                  |
| `set`                                   | 允许 list,tuple,set,frozenset,deque, 或生成器和转换为集合                                 |
| `frozenset`                             | 允许 list,tuple,set,frozenset,deque, 或生成器和强制转换为冻结集                           |
| `deque`                                 | 允许 list,tuple,set,frozenset,deque, 或生成器和强制转换为双端队列                         |
| **datetime 包定义的类型**               |                                                                                           |
| `date`                                  |                                                                                           |
| `datetime`                              |                                                                                           |
| `time`                                  |                                                                                           |
| `timedelta`                             |                                                                                           |
| **typing 包定义的类型**                 |                                                                                           |
| `Any`                                   | 具有任意类型的动态类型值                                                                  |
| `Iterable[...]`                         | 可迭代对象                                                                                |
| `Deque[...]`                            | 队列类型                                                                                  |
| `Dict[...,...]`                         | 字典类型                                                                                  |
| `FrozenSet`                             |                                                                                           |
| `List[...]`                             | 列表类型                                                                                  |
| `Optional[...]`                         | 可选值类型，若不存在则为 None                                                             |
| `Sequence[...]`                         | 序列                                                                                      |
| `Set[...]`                              | 集合                                                                                      |
| `Tuple[...]`                            | 元组                                                                                      |
| `Union`                                 | 联合类型，多种类型可选，示例：`Union[str, int, float]`                                    |
| `Callable`                              |                                                                                           |
| `Literal`                               | 字面量。它在定义简单的枚举值时非常好用。示例：`MODE = Literal['r', 'rb', 'w', 'wb']`      |
| `Pattern`                               |                                                                                           |
| **enum 包定义的类型**                   |                                                                                           |
| `Enum`                                  |                                                                                           |
| **其他**                                |                                                                                           |
| `decimal.Decimal`                       |                                                                                           |
| `pathlib.Path`                          |                                                                                           |
| `uuid.UUID`                             |                                                                                           |

## pydantic 定义的类型

| 类型            | 描述                                                                                       |
| --------------- | ------------------------------------------------------------------------------------------ |
| `FilePath`      | 与 Path 类似，但路径必须存在并且必须是文件                                                 |
| `DirectoryPath` | 与 Path 类似，但路径必须存在并且必须是目录                                                 |
| **约束类型**    | 可以使用 `con*` 类型函数来约束许多常见类型的值                                             |
| `conbytes`      | 用于约束 bytes 的类型方法，示例：`short_bytes: conbytes(min_length=2, max_length=10)`      |
| `condecimal`    | 用于约束 Decimal 的类型方法，示例：`condecimal(gt=0)`                                      |
| `confloat`      | 用于约束 float 的类型方法，示例：`confloat(multiple_of=0.5)`                               |
| `conint`        | 用于约束 int 的类型方法，示例：`big_int: conint(gt=1000, lt=1024)`                         |
| `conlist`       | 用于约束 list 的类型方法，示例：`conlist(int, min_items=1, max_items=4)`                   |
| `conset`        | 用于约束 set 的类型方法，示例：`conset(int, min_items=1, max_items=4)`                     |
| `constr`        | 用于约束 str 的类型方法，示例：`regex_str: constr(regex=r'^apple (pie\|tart\|sandwich)$')` |