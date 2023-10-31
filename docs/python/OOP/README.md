# 面向对象

<!--
## 类和对象

### 类属性

类自身可以具有自己的属性，被称为类属性，或者类成员变量。类属性可以直接通过类名访问，也可以通过实例访问。

类属性在创建实例时并不会被单独创建，都是引用的类的属性，它们在内存中只有一份。同样可以通过实例来改变类属性，此时将进行拷贝动作，该实例的类属性将脱离类的属性，实现了属性的解绑定，把原来类属性覆盖了，该属性成为了实例的私有属性，其他实例不会受影响。

## 类变量和实例变量

- 类变量：类变量在整个实例化的对象中是公用的。类变量定义在类中且在函数体之外。访问或调用类变量的正确方式是 `类名.变量名` 或者 `self.__class__.变量名`。`self.__class__` 自动返回每个对象的类名
- 实例变量：定义在方法中的变量，属于某个具体的对象。访问或调用实例变量的正确方式是 `对象名.变量名` 或者 `self.变量名`

## 获取类型

- 通过 `type()` 内置函数获取对象类型
- 使用类名加 `.__class__` 获取对象的类型

## 类的特殊方法

### `__dir__`

`__dir__()` 方法用于类的所有属性和方法名，它是一个字典，内置函数 `dir()` 就是对它的调用

### `__str__`

`__str__` 方法用于 `str()` 函数转换中，默认使用 `print()` 方法打印一个对象时，就是对它的调用，我们可以重写这个函数还实现自定义类向字符串的转换。

### `__dict__` 属性

在没使用 `__slots__` 的情况下，`__new__` 会为每个实例创建一个 `__dict__` 字典，来保存实例的所有属性。在进行属性寻找的时候，会先在对象的 `__dict__` 中找，找不到，就去类的 `__dict__` 中找，再找不到就去父类的 `__dict__` 中找。

### `__call__`

还有一些特殊方法没有在基类中实现，但是它们具有非常特殊的功能，比如 `__call__()` 可以将一个对象名函数化。实现了 `__call__()` 函数的类，其实例就是可调用的（Callable）。 可以像使用一个函数一样调用它。

```py
class Employee():
    def __init__(self, id, name):
        self.id = id
        self.name = name

    def __call__(self, *args):
        print(*args)
        print('Printed from __call__')

worker0 = Employee(0, "John")
worker0("arg0", "arg1")

>>>
arg0 arg1
Printed from __call__
```

装饰器类 就是基于 `__call__()` 方法来实现的。注意 `__call__()` 只能通过位置参数来传递可变参数，不支持关键字参数，除非函数明确定义形参。

可以使用 `callable()` 来判断一个对象是否可被调用，也即对象能否使用()括号的方法调用。

```py
# 如果 Employee 类不实现 __call__，则返回 False
callable(worker0)

>>>
True
```

### 属性方法

在基类中提供了3个与属性操作相关的方法：

- `__delattr__`，用于 del 语句，删除类或者对象的某个属性
- `__setattr__`，用于动态绑定属性
- `__getattribute__`，在获取类属性时调用，无论属性是否存在

```py
class C():
    def __init__(self):
        self.hello = "123"

    def __delattr__(self, name):
        print("delattr %s" % name)
        super().__delattr__(name)       # 调用 object 的 __delattr__

    def __setattr__(self, attr, value):
        print("setattr %s" % attr)
        super().__setattr__(attr, value)# 调用 object 的 __setattr__

c = C()
del c.hello      # 调用类对象的 __delattr__
print(c.hello)   # 报错 hello 属性不存在

c.newarg = "100" # 调用类对象的 __setattr__
>>>
delattr hello
setattr newarg
```

在于 Python 提供了三个内置方法 `getattr()`，`setattr()` 和 `hasattr()`，它们均是基于类的属性方法来实现的。

### 逻辑运算

基类默认支持所有逻辑运算：`__eq__`，`__ge__`，`__gt__`，`__le__`， `__lt__` 和 `__ne__`。

默认使用对象的地址，也即 id 比较。

### `__enter__` 和 `__exit__`

Python 的 `with as` 语句提供自动回收资源的魔法。实际上就是调用的对象的这两个方法完成的。

-->