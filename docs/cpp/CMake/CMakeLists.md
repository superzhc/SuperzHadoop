# `CMakeLists.txt`

## 内置变量

| 变量                       | 描述                                          |
| -------------------------- | --------------------------------------------- |
| `CMAKE_SOURCE_DIR`         | 根源目录                                      |
| `CMAKE_CURRENT_SOURCE_DIR` | 如果使用子项目和目录，则为当前子项目源目录    |
| `PROJECT_SOURCE_DIR`       | 当前 cmake 项目的源目录                       |
| `CMAKE_BINARY_DIR`         | 根二进制文件生成目录。这是运行cmake命令的目录 |
| `CMAKE_CURRENT_BINARY_DIR` | 当前所处的生成目录                            |
| `PROJECT_BINARY_DIR`       | 当前项目的生成目录                            |
| `EXECUTABLE_OUTPUT_PATH`   | 目标二进制可执行文件的存放位置                |

## 基本命令

### `cmake_minimum_required`

> 指定 CMake 的版本

```shell
cmake_minimum_required(VERSION 3.10)
```

### `project`

> 设定工程名和版本号

```shell
# 设定工程名和版本号
project(SUPERZ VERSION 1.0)

## project函数将创建一个变量 ${PROJECT_NAME}，在其他函数中可以直接使用该变量
```

### `set`

> 设置一系列变量的值

```shell
# 启动对C11标准的支持
set(CMAKE_C_STANDARD 11)
# 显式要求指明支持C标准
set(CMAKE_C_STANDARD_REQUIRED True)
# 启用C99标准
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")

# 设置C++标准为 C++ 11
set(CMAKE_CXX_STANDARD 11)
# 设置C++编译器的位置
set(CMAKE_CXX_COMPILER, "C:\\MinGW\\bin\\g++")
# 启用C++ 11标准
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
# 启用警告
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")

# 自定义变量
set(CUSTOM_SOURCES my_structure.c my_pointer.c)
```

> 在 CMake 中，`${}` 的语法含义是获取变量的值

### `aux_source_directory`

> 将指定目录下的所有源文件存储到一个变量

```sh
aux_source_directory(. SOURCES_LIST)
```

> 该操作也有一个弊端，它会将指定目录下的所有源文件都获取到文件列表中，对于不需要的源文件无法排除

### `add_executable`

> 从指定的源文件构建可执行文件

```sh
# add_executable 的第一个参数是要生成的可执行文件的名称，第二个参数是要编译的源文件的列表
add_executable(superz-learning-c
                # 对于多个源文件，只需要在该处添加所有源文件即可
                my_structure.c
                my_pointer.c)

# 使用 aux_source_directory 设置的源文件列表
add_executable(superz-learning-c ${SOURCES_LIST})
```

### `include_directories`

> 指定头文件的搜索位置，多个搜索路径之间使用空格进行分隔。
> 
> 头文件不但对编译是必须的，也可以被 CLion 索引，以提供代码自动完成、代码导航。

依据操作系统的不同，编译器会自动搜索一些预定义的位置，用户可以手工添加：

```shell
# 可选的BEFORE/AFTER关键字用于控制搜索顺序
include_directories(BEFORE ${MY_SOURCE_DIR}/src )
```

如果未设置 include_directories 命令且源文件 include 头文件，则需要将源文件 include 的头文件的相对路径补全。

### `target_include_directories`

```shell
# 指定项目编译的时候需要include的文件路径，PROJECT_BINARY_DIR变量为编译发生的目录，也就是make执行的目录，PROJECT_SOURCE_DIR为工程所在的目录 
# target_include_directories官方文档：https://cmake.org/cmake/help/v3.3/command/target_include_directories.html 
target_include_directories(${PROJECT_LIBRARY_NAME}
                            PUBLIC 
                            ${PROJECT_BINARY_DIR} 
                           ) 
```

### `link_directories`

> 用来指定编译器搜索库文件的路径。

### `find_package`

**语法**

```sh
find_package(<PackageName>
            # 指定包的版本，若指定版本则检查包的版本是否和 version 兼容，EXACT表示必须完全匹配的版本而不是兼容版本就可以
            [version] [EXACT] 
			[QUIET]
            # 模块是否是必需的
            [REQUIRED] 
            # 要查找的库的列表
            [[COMPONENTS] [components...]] 
            [OPTIONAL_COMPONENTS components...]
            [MODULE|CONFIG|NO_MODULE]
            [NO_POLICY_SCOPE]
            [NAMES name1 [name2 ...]]
            [CONFIGS config1 [config2 ...]]
            [HINTS path1 [path2 ... ]]
            [PATHS path1 [path2 ... ]]
            [PATH_SUFFIXES suffix1 [suffix2 ...]]
            [NO_DEFAULT_PATH]
            [NO_PACKAGE_ROOT_PATH]
            [NO_CMAKE_PATH]
            [NO_CMAKE_ENVIRONMENT_PATH]
            [NO_SYSTEM_ENVIRONMENT_PATH]
            [NO_CMAKE_PACKAGE_REGISTRY]
            [NO_CMAKE_BUILDS_PATH] # Deprecated; does nothing.
            [NO_CMAKE_SYSTEM_PATH]
            [NO_CMAKE_SYSTEM_PACKAGE_REGISTRY]
            [CMAKE_FIND_ROOT_PATH_BOTH | ONLY_CMAKE_FIND_ROOT_PATH | NO_CMAKE_FIND_ROOT_PATH]
            )
```

*示例*

```sh
# 在系统中查找并加载线程库
find_package(Threads)
```

### `target_link_libraries`

target_link_libraries 函数是将预先编译好的库（通常是 `.a` 或 `.so` 文件）链接到项目的 `.o` 文件，使得项目可以使用这些库中的函数和类：

```shell
# 使用BOOST库
find_package(Boost)
if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIR})
endif()
set(Boost_USE_STATIC_LIBS OFF) # enable dynamic linking
set(Boost_USE_MULTITHREAD ON)  # enable multithreading
find_package(Boost COMPONENTS REQUIRED chrono filesystem)
# 声明链接到BOOST库
target_link_libraries(my_target ${Boost_LIBRARIES})
```

### `add_library`

> 指定从某些源文件创建库

```shell
# 第一个参数：指定库的名称
# 第二个参数：指定库的类型，静态库、动态库，默认是静态库
# 第三个参数：指定生成库的源文件
add_library (my_library STATIC|SHARED|MODULE ${SOURCE_FILES})
```

### `add_subdirectory`

> 用于包含子工程。

一个工程可以依赖于其它工程，CMake 没有类似于 VS 的解决方案（Solution）的概念，但是它允许用户手工定义工程之间的依赖关系。

典型的，用户希望在工作区中这样管理多工程（Multi-project）结构：

1. 打开主工程时，其依赖的工程一并打开
2. 主工程的设置自动应用到被依赖的工程
3. 重构、代码完成等可以影响到所有工程

上面的三点需求可以通过合适的 `CMakeList.txt` 完成，用户需要把上述所有工程组织到 `CMakeList.txt` 所在目录之下，形成树形结构，每个子目录对应一个子工程，并且子目录有自己的 `CMakeLists.txt`。最后，在根目录的 `CMakeLists.txt` 中添加：

```shell
add_subdirectory(project1) # 把project1包含到主工程
add_subdirectory(project2) # 把project2包含到主工程
```

### `install`

> 指定要在安装时运行的规则

```
install(TARGETS <target>... [...])
install({FILES | PROGRAMS} <file>... [...])
install(DIRECTORY <dir>... [...])
install(SCRIPT <file> [...])
install(CODE <code> [...])
install(EXPORT <export-name> [...])
```