# `find_package`

> `find_package` 是用于发现和设置包的 CMake 模块的命令。这些模块包含 CMake 命令，用于标识系统标准位置中的包。CMake 模块文件称为 `Find<name>.cmake`，当调用 `find_package(<name>)` 时，模块中的命令将会运行。
>
> 除了在系统上实际查找包模块之外，查找模块还会设置了一些有用的变量，反映实际找到了什么，也可以在用户自己的 `CMakeLists.txt` 中使用这些变量。对于某模块，如 SomeModule，相关模块为 `FindSomeModule.cmake` 通常附带的设置了一些 CMake 变量。

每一个模块都会定义以下几个变量：

- `<LibaryName>_FOUND`
- `<LibaryName>_INCLUDE_DIR` or `<LibaryName>_INCLUDES`
- `<LibaryName>_LIBRARY` or `<LibaryName>_LIBRARIES`

CMake 官方预定义的依赖包的 Module 存储在 `<CMake_Path>/share/cmake-<version>/Modules` 目录下。预定义的 Module 是找寻本机标准安装的库的目录。

用户指定的 CMake 路径通过变量 `CMAKE_MODULE_PATH` 进行设置。

