# `CMakeLists.txt`

## 基本命令

### `cmake_minimum_required`

指定 CMake 的版本：

```shell
cmake_minimum_required(VERSION 3.10)
```

### `project`

设定工程名和版本号

```shell
# 设定工程名和版本号
project(SUPERZ VERSION 1.0)
```

### `set`

设置一系列变量的值：

```shell
# 设置C编译器的位置
set (CMAKE_CXX_COMPILER, "C:\\MinGW\\bin\\g++")
# 启用C99标准
set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")
# 启用C++ 11标准
set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
# 启用警告
set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")
```

> 在 CMake 中，`${}` 的语法含义是获取变量的值

### `add_executable`

添加可执行的构建目标

### `include_directories`

指定头文件的搜索位置。头文件不但对编译是必须的，也可以被 CLion 索引，以提供代码自动完成、代码导航。

依据操作系统的不同，编译器会自动搜索一些预定义的位置，用户可以手工添加：

```shell
# 可选的BEFORE/AFTER关键字用于控制搜索顺序
include_directories(BEFORE ${MY_SOURCE_DIR}/src )
```

### `target_include_directories`

```shell
# 指定项目编译的时候需要include的文件路径，PROJECT_BINARY_DIR变量为编译发生的目录，也就是make执行的目录，PROJECT_SOURCE_DIR为工程所在的目录 
# target_include_directories官方文档：https://cmake.org/cmake/help/v3.3/command/target_include_directories.html 
target_include_directories(CalculateSqrt PUBLIC 
                           "${PROJECT_BINARY_DIR}" 
                           ) 
```

### `link_directories`

`link_directories` 用来指定编译器搜索库文件的路径。

### `target_link_libraries`

target_link_libraries 函数是将预先编译好的库（通常是 `.a` 或 `.so` 文件）链接到项目的 `.o` 文件，使得项目可以使用这些库中的函数和类：

```shell
# 使用BOOST库
find_package(Boost)
if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIR})
endif()
set (Boost_USE_STATIC_LIBS OFF) # enable dynamic linking
set (Boost_USE_MULTITHREAD ON)  # enable multithreading
find_package (Boost COMPONENTS REQUIRED chrono filesystem)
# 声明链接到BOOST库
target_link_libraries (my_target ${Boost_LIBRARIES})
```

### `find_package`

`find_package(Threads)` 是 CMake 中的一个指令，用于在系统中查找并加载线程库。它会查找线程库（通常是 pthreads 或 Windows Threads）并设置一些 CMake 变量，以便在编译和链接时使用该库。

### `add_library`

添加库：

```shell
add_library (my_library STATIC|SHARED|MODULE ${SOURCE_FILES})
```

### `add_subdirectory`

用于包含子工程。

一个工程可以依赖于其它工程，CMake 没有类似于 VS 的解决方案（Solution）的概念，但是它允许用户手工定义工程之间的依赖关系。

典型的，用户希望在工作区中这样管理多工程（Multi-project）结构：

1. 打开主工程时，其依赖的工程一并打开
2. 主工程的设置自动应用到被依赖的工程
3. 重构、代码完成等可以影响到所有工程

上面的三点需求可以通过合适的 `CMakeList.txt` 完成，用户需要把上述所有工程组织到 `CMakeList.txt` 所在目录之下，形成树形结构，每个子目录对应一个子工程，并且子目录有自己的 `CMakeLists.txt`。最后，在根目录的 `CMakeLists.txt` 中添加：

```shell
add_subdirectory (project1) # 把project1包含到主工程
add_subdirectory (project2) # 把project2包含到主工程
```