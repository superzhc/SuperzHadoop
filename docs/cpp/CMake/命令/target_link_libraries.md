# `target_link_libraries`

> 链接动态库

**语法**

```cmake
target_link_libraries(
    <target> 
    <PRIVATE|PUBLIC|INTERFACE> <item>... 
    [<PRIVATE|PUBLIC|INTERFACE> <item>...]...)
```

- target：指定要加载动态库的文件的名字
    - 该文件可能是一个源文件
    - 该文件可能是一个动态库文件
    - 该文件可能是一个可执行文件
- PRIVATE|PUBLIC|INTERFACE：动态库的访问权限，默认为 PUBLIC