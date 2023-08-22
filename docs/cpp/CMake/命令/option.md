# `option`

**语法**

```
option(<option_variable> "help string" [initial value])
```

- `<option_variable>` 表示该选项的变量的名称。
- `"help string"` 记录选项的字符串，在 CMake 的终端或图形用户界面中可见。
- `[initial value]` 选项的默认值，可以是 ON 或 OFF。

通过 option 设置的选项变量，用户可以在命令行通过 `-D` 进行选址值传递。