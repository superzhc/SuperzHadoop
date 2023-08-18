# CMake

CMake 是个一个开源的跨平台自动化建构系统，用来管理软件建置的程序，并不依赖于某特定编译器，并可支持多层目录、多个应用程序与多个库。 它用配置文件控制建构过程（build process）的方式和 Unix 的 make 相似，只是 CMake 的配置文件取名为 `CMakeLists.txt`。CMake 并不直接建构出最终的软件，而是产生标准的建构档（如 Unix 的 Makefile 或 `Windows Visual C++` 的 projects/workspaces），然后再依一般的建构方式使用。

> CMake 允许开发者编写一种平台无关的 `CMakeLists.txt` 文件来定制整个编译流程，然后再根据目标平台进一步生成目标平台的 Makefile 和工程文件。

## 下载安装

> 官网下载地址：<https://cmake.org/download/>
>
> Github Releases：<https://github.com/Kitware/CMake/releases>

根据操作系统下载指定的二进制版本即可安装。

## cmake 命令编译步骤

1. 建立编译目录
2. 执行 cmake 命令，该命令会配置工程并建立一个本地构建环境
3. 执行上一步建立的构建环境去完成实际的编译/链接工程

**示例**

```sh
# 项目所在源码位置：D:/code/cmake-demo/step1
# 建立编译目录
mkdir D:/code/cmake-demo/build_step1
# 执行 cmake 命令
cd D:/code/cmake-demo/build_step1
cmake ../step1

# 注意：执行 build 一定需要指定构建的环境路径
# 执行上一步建立的构建环境
cmake --build .
```

## 使用

| 选项      | 描述                                                  |
| --------- | ----------------------------------------------------- |
| `-v`      | 构建时获得详细的输出（CMake 3.14+）                   |
| `--trace` | 打印运行的 CMake 的每一行                             |
| `-G`      | 指定生成器                                            |
| `-D`      | 设置选项                                              |
| `-L`      | 列出所有选项，或者使用 `-LH` 列出人类更易读的选项列表 |


**查看 cmake 版本**

```sh
cmake --version
```

**`-D` 选项**

> 设置缓存变量，只要第一次进行了配置，后续构建过程中该变量会被保留。

*示例*

```sh
cmake -B build -DCMAKE_BUILD_TYPE=Release
```

**`-G` 选项**

> 指定要用的生成器(generator)

*示例*

```sh
cmake --build build -G "Visual Studio 16 2019"
```

## 构建类型

默认情况下的构建类型是 DEBUG 模式。

可以通过 `CMAKE_BUILD_TYPE` 变量来设置构建类型，支持的构建类型为：

- Debug:调试模式，完全不优化，生成调试信息，方便调试程序
- Release:发布模式，优化程度最高，性能最佳，但是编译比 Debug 慢
- MinSizeRel:最小体积发布，生成的文件比 Release 更小，不完全优化，减少二进制体积
- RelWithDebInfo:带调试信息发布，生成的文件比 Release 更大，因为带有调试的符号信息，这是为了让用户在使用程序出错时，能够反馈一些信息

