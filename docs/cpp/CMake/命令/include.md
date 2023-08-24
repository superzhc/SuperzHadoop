# `include`

- `INCLUDE(file1 [OPTIONAL])`
- `INCLUDE(module [OPTIONAL])`

用来载入 `CMakeLists.txt` 文件，也用于载入预定义的 cmake 模块。你可以指定载入一个文件,如果定义的是一个模块，那么将在 `CMAKE_MODULE_PATH` 中搜索这个模块并载入。载入的内容将在处理到 INCLUDE 语句是直接执行。