# 结构体

> C 语言中的结构体是对一组数据元素的有序组织

**定义**

结构体定义了一个包含多个成员的新的数据类型，结构体语句的格式如下：

```c
struct tag {
    member-list
    member-list
    member-list
    ...
} variable-list ;
```

- tag 是结构体标签
- member-list 是标准的变量定义，比如 int i; 或者 float f;，或者其他有效的变量定义
- variable-list 结构变量，定义在结构的末尾，最后一个分号之前，可以指定一个或多个结构变量

> 在一般情况下，tag、member-list、variable-list 这 3 部分至少要出现 2 个。

**声明变量**

```c
// 声明结构体类型变量
struct structName varName;

// 声明结构体类型的指针变量
struct structName *pointVarName;
```

**调用成员变量**

访问一个结构体对象中某个成员使用 `.` 操作符。

**结构体类型指针调用成员变量**

访问一个指向结构体类型的指针，使用 `->` 操作符。