# `ExternalProject` 模块

## `ExternalProject_Add()`

> `ExternalProject_Add` 函数用于添加第三方源

```cmake
ExternalProject_Add(<name> [<option>...])
```

**目录选项**

- PREFIX：外部项目的根目录
- TMP_DIR：存储临时文件的目录
- STAMP_DIR：用于存储每个步骤的时间戳的目录
- LOG_DIR：用于存储每个步骤的日志的目录
- DOWNLOAD_DIR：解压缩包前存储下载文件的目录。此目录仅由 URL下载方法使用，所有其他下载方法使用 SOURCE_DIR。
- SOURCE_DIR：将下载内容解压缩包到的源目录，或对于非 URL下载方法，应在签出、克隆存储库等的目录。如果未指定下载方法，则必须指向外部项目已解包或克隆/签出的现有目录。
    - 注意：如果指定了下载方法，则可能会删除源目录的任何现有内容。在开始下载之前，只有URL下载方法会检查此目录是否丢失或为空，如果不是空的，则会出现错误停止。所有其他下载方法都会自动放弃源目录中以前的任何内容。
- BINARY_DIR：指定生成目录位置。如果 BUILD_IN_SOURCE 启用，则忽略此选项。
- INSTALL_DIR

如果以上任何一项未指定 `_DIR` 选项，则默认值如下：

```cmake
TMP_DIR      = <prefix>/tmp
STAMP_DIR    = <prefix>/src/<name>-stamp
DOWNLOAD_DIR = <prefix>/src
SOURCE_DIR   = <prefix>/src/<name>
BINARY_DIR   = <prefix>/src/<name>-build
INSTALL_DIR  = <prefix>
LOG_DIR      = <STAMP_DIR>
```

若未指定 `PREFIX`、`EP_PREFIX` 或 `EP_BASE`，则默认设置 PREFIX 为 `<name>-PREFIX`。

**下载选项**

- URL
  ```cmake
  # 外部项目源的路径或 URL 列表。当给出多个 URL 时，会依次尝试，直到其中一个成功。
  URL <url1> [<url2>...]
  ```
- URL_HASH
  ```cmake
  # 要下载的存档文件的哈希。参数的形式应该是<algo>=<hashValue>，其中algo可以是file（）命令支持的任何哈希算法。强烈建议URL下载时指定此选项，因为它可以确保下载内容的完整性。它还用于检查以前下载的文件，如果本地目录中已有与指定哈希匹配的早期下载文件，则可以完全避免连接到远程位置。
  URL_HASH<algo>=<hashValue>
  ```
- DOWNLOAD_NO_PROGRESS：可用于禁用记录下载进度。如果未给出此选项，将记录下载进度消息。

**更新和补丁选项**

**配置选项**

- CONFIGURE_COMMAND
  ```cmake
  默认的 configure 命令根据主项目运行带有几个选项的 CMake，添加的选项通常只是使用与主项目相同的生成器所需的选项，但可以使用 CMAKE_GENERATOR 选项来覆盖此选项。
  对于非 CMake 外部项目，必须使用 CONFIGURE_COMMAND 选项覆盖默认的配置命令（支持生成器表达式）。
  对于不需要配置步骤的项目，使用空字符串指定此选项作为要执行的命令。
  ```
- CMAKE_COMMAND
  ```cmake
  为配置步骤指定另一个 cmake 可执行文件（使用绝对路径）。通常不建议这样做，因为通常希望在整个构建过程中使用相同的 CMake 版本。如果已使用 CONFIGURE_COMMAND 指定自定义配置命令，则忽略此选项。
  ```
- CMAKE_GENERATOR
  ```cmake
  重写用于配置步骤的 CMake 生成器。如果没有此选项，将使用与主版本相同的生成器。如果使用 CONFIGURE_COMMAND 选项指定了自定义配置命令，则忽略此选项。
  ```
- CMAKE_GENERATOR_PLATFORM
  ```cmake
  将特定于生成器的平台名称传递给 CMake 命令。在没有 CMAKE_GENERATOR 选项的情况下提供此选项是错误的。
  ```
- CMAKE_GENERATOR_TOOLSET
  ```cmake
  将特定于生成器的工具集名称传递给 CMake 命令。在没有 CMAKE_GENERATOR 选项的情况下提供此选项是错误的。
  ```
- CMAKE_GENERATOR_INSTANCE
  ```cmake
  将特定于生成器的实例选择传递给 CMake 命令。在没有 CMAKE_GENERATOR 选项的情况下提供此选项是错误的。
  ```
- CMAKE_ARGS
  ```cmake
  指定的参数被传递到cmake命令行。它们可以是cmake命令理解的任何参数，而不仅仅是-D定义的缓存值参数
  ```
- CMAKE_CACHE_ARGS
  ```cmake
  这是另一种指定缓存变量的方法，在这种情况下，命令行长度问题可能会成为一个问题。参数的格式应为 var:STRING=value。
  ```

**构建选项**

- BUILD_ALWAYS 

**安装选项**

- INSTALL_COMMAND

**测试选项**