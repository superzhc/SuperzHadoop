# `cmake_minimum_required`

> 设置 CMake 的使用版本

**示例**

```txt
cmake_minimum_required(VERSION 3.1)

# 从 CMake 3.12 开始，版本号可以声明为一个范围，例如 VERSION 3.1...3.15；这意味着这个工程最低可以支持 3.1 版本，但是也最高在 3.15 版本上测试成功过。
cmake_minimum_required(VERSION 3.7...3.21)
```