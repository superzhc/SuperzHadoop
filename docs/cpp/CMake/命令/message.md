# `message`

> 输出调试信息

**语法**

```
message([<mode>] "message text" ...)
```

输出常常可以用来对 `CMakeLists.txt` 进行调试。其中 `[<mode>]` 是一个可选项，如果不填就是普通的输出，如果填入以下选项，将有特定功能:

- 为空：重要消息
- `FATAL_ERROR`：会输出消息，然后停止处理 `CMakeLists.txt`，当然也不会生成 Makefile
- `SEND_ERROR`：会输出消息，但不会停止处理 `CMakeLists.txt`，然而不会生成 Makefile，也就是说如果后面还有其他的 ERROR，也可以被输出
- `AUTHOR_WARNING`：CMake 警告 (dev), 会继续执行
- `WARNING`：会输出一个警告消息，推荐使用这个
- `STATUS`：非重要消息