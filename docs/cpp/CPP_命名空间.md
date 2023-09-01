# 命名空间

> 命名空间支持是 C++ 的一个新特性。

C++ 编译器的标准组件，如类、函数和变量，它们都默认在命名空间 `std` 中。

**定义命名空间**

命名空间的定义使用关键字 namespace，后跟命名空间的名称，如下所示：

```cpp
namespace namespace_name {
   // 代码声明
}
```

为了调用带有命名空间的函数或变量，需要在前面加上命名空间的名称，如下所示：

```cpp
name::code;  // code 可以是变量或函数
```

**using 指令**

可以使用 `using namespace 指令`，这样在使用命名空间时就可以不用在前面加上命名空间的名称。这个指令会告诉编译器，后续的代码将使用指定的命名空间中的名称。

using 指令也可以用来指定命名空间中的特定项目。例如，如果只打算使用 std 命名空间中的 cout 部分，您可以使用如下的语句：

```cpp
using std::cout;
```

**嵌套的命名空间**

命名空间可以嵌套，可以在一个命名空间中定义另一个命名空间，如下所示：

```cpp
namespace namespace_name1 {
   // 代码声明
   namespace namespace_name2 {
      // 代码声明
   }
}
```

可以通过使用 `::` 运算符来访问嵌套的命名空间中的成员：

```cpp
// 访问 namespace_name2 中的成员
using namespace namespace_name1::namespace_name2;
 
// 访问 namespace_name1 中的成员
using namespace namespace_name1;
```