# 枚举类型

用枚举类型（enumerated type）声明符号名称来表示整型常量。使用 enum 关键字，可以创建一个新“类型”并指定它可具有的值。

*示例*

```c
// 定义枚举
enum spectrum {red, orange, yellow, green, blue, violet};

// 声明枚举变量
enum spectrum color;
```

**默认值**

> 默认情况下，枚举列表中的常量都被赋予 0、1、2 等。

**赋值**

在枚举声明中，可以为枚举常量指定整数值。

*示例*

```c
enum levels {low=100, medium=500, high=2000};
```