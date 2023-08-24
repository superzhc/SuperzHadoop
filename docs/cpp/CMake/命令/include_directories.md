# `include_directories`

```cmake
include_directories([AFTER|BEFORE] [SYSTEM] dir1 dir2 ...)
```

这条指令可以用来向工程添加多个特定的头文件搜索路径，路径之间用空格分割，如果路径中包含了空格，可以使用双引号将它括起来，默认的行为是追加到当前的头文件搜索路径的后面。