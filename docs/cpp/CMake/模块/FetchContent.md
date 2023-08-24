# `FetchContent` 模块

该模块主要包括四个函数：

1. `FetchContent_Declare(<name> <contentOptions>...)`: 描述如何下载依赖库，name 声明下载库的名称，contentOptions 描述获取、更新外部库的方式（常用的有通过 Git Repo下载，通过 URL 下载等）；
2. `FetchContent_MakeAvaliable(<name1> [<name2>...])`: 构建命令所声明的 `name1, name2, ...`；
3. `FetchContent_Populate(<name>)`: 构建命令所声明的 name 依赖， 该命令会定义三个变量：
    - `<lowercaseName>_POPULATED`: 依赖是否已被构建
    - `<lowercaseName>_SOURCE_DIR`: 依赖存储路径
    - `<lowercaseName>_BINARY_DIR`: 依赖 Build 路径
  - 若 FetchContent_Populate 不止有 name 参数，此时，不再使用 FetchContent_Declare 所定义的配置，而是由 FetchContent_Populate 给出，不再定义 `<lowercaseName>_POPULATED`，不会在全局定义`<lowercaseName>_SOURCE_DIR` 和 `<lowercaseName>_BINARY_DIR`，但仍会在当前作用域内定义，不再检测是否已经构建该依赖，具体语法为:
    ```
    FetchContent_Populate(
    <name>
    [QUIET]
    [SUBBUILD_DIR <subBuildDir>]
    [SOURCE_DIR <srcDir>]
    [BINARY_DIR <binDir>]
    ...
    )
    ```
    其中，QUIET 表示隐藏与激活依赖相关的输出；SUBBUILD_DIR 用于指定 sub-build 路径；SOURCE_DIR 用于指定 source 路径，BINARY_DIR 用于指定 binary 路径，其余参数均会传递给 ExternalProject_Add()命令。
4. `FetchContent_GetProperties(<name> [SOURCE_DIR <srcDirVar>] [BINARY_DIR <binDirVar>] [POPULATED <doneVar>])`: 获取与外部依赖 name 相关的属性。

## 最佳实践

1. 总是优先使用 FetchContent_MakeAvailable 而不是 FetchContent_Populate。
2. 总是优先使用 FetchContent_Declare 声明所有依赖，再使用 FetchContent_MakeAvailable 或 FetchContent_Populate 构建依赖，确保当前项目能够控制依赖的具体内容，这是因为 FetchContent_Declare 不会覆盖先前声明。
3. FetchContent_MakeAvailable 会先检查依赖是否已经构建完成，因此不会重复构建，但 FetchContent_Populate 并不会，重复构建会报错，因此，使用 FetchContent_Populate 前，必须使用 FetchContent_GetProperties 获取变量 `<lowercaseName>_POPULATED`，检测是否需要构建该依赖。