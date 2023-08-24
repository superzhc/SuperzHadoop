# `file`

- `file(WRITE filename "message to write"... )`
- `file(APPEND filename "message to write"... )`
- `file(READ filename variable)`
- `file(GLOB variable [RELATIVE path] [globbing expressions]...)`
- `file(GLOB_RECURSE variable [RELATIVE path] [globbing expressions]...)`
- `file(REMOVE [directory]...)`
- `file(REMOVE_RECURSE [directory]...)`
- `file(MAKE_DIRECTORY [directory]...)`
- `file(RELATIVE_PATH variable directory file)`
- `file(TO_CMAKE_PATH path result)`
- `file(TO_NATIVE_PATH path result)`

## 搜索指定路径下的文件或目录

> 将指定路径下搜索到的满足条件的所有文件名或目录名生成一个列表，并将其存储到变量中

```cmake
file(GLOB 变量名 要搜索的文件路径和文件类型)
```

**示例**

```cmake
file(GLOB MAIN_SRC ${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp)
file(GLOB MAIN_HEAD ${CMAKE_CURRENT_SOURCE_DIR}/include/*.h)
```

## 递归搜索指定路径下的文件及目录

```cmake
file(GLOB_RECURSE variable [RELATIVE path] [globbing expressions]...)
```