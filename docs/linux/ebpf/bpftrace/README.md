# bpftrace

> bpftrace 通过高度抽象的封装来使用 eBPF，大多数功能只需要寥寥几笔就可以运行起来。

## 语法

bpftrance 本质上是一种脚本语言，风格与 AWK 非常相似，脚本的结构：

```c
// 注释用法：
// 单行注释

/*
 * 多行注释
 */
​
BEGIN{
  // 开始时执行一次的代码

  /*
   * bpftrace 支持三目条件运算符
   */
   pid & 1 ? printf("Odd\n") : printf("Even\n");
}
/* /filter/ 是可选的 */
probe[,probe,...] /filter/ {
  
  /*
   * 支持 if-else 表达式
   */
   @reads = count();
   if (args.count > 1024){
    @large = count();
   }

   /*
    * unroll(){...} 进行循环好像？？？
    */
    $i = 1; unroll(5) { printf("i: %d\n", $i); $i = $i + 1; }

    /*
     * C 风格的循环结构，支持 continue、break 关键字
     */
     $i = 0; while ($i <= 100) { printf("%d ", $i); $i++}
}
END{
  // 结束时执行
}
```

**变量**

*语法*

```c
// 全局变量
@global_name
// 线程变量
@thread_local_variable_name[tid]、

$scratch_name
```

**常量**

> bpftrace 支持 *整数型*、*字符型* 以及 *字符串* 常量。
>
> 字符型常量使用单引号包裹起来，例如：`'a'`、`'@'`；字符串使用双引号包裹起来，例如：`"a string"`。
>
> 注意：bpftrace 不支持浮点型

```sh
# bpftrace -e 'BEGIN { printf("%lu %lu %lu", 1000000, 1e6, 1_000_000)}'
Attaching 1 probe...
1000000 1000000 1000000
```

**结构体**

> 在 bpftrace 中，用户可以自定义结构体。

*示例*

```c
// from fs/namei.c:
struct nameidata {
        struct path     path;
        struct qstr     last;
        // [...]
};
```

## 使用

```sh
# 执行脚本文件
bpftrace [options] filename

bpftrace [options] - <stdin input>

# 执行单行指令
bpftrace [options] -e 'program'
```

**选项**

| 选项             | 描述                                                                           |
| ---------------- | ------------------------------------------------------------------------------ |
| `-B MODE`        | 输出缓冲的模式 ('line', 'full', or 'none')                                     |
| `-d`             | 输出运行状态的 Debug 信息                                                      |
| `-dd`            | 输出运行状态的 Debug 详细信息                                                  |
| `-e 'program'`   | 执行 `program` 代码                                                            |
| `-h`             | 展示帮助信息                                                                   |
| `-I DIR`         | add the specified DIR to the search path for include files                     |
| `--include FILE` | adds an implicit #include which is read before the source file is preprocessed |
| `-l [search]`    | 列出探针                                                                       |
| `-p PID`         | 开启指定 PID 的 USDT 探针                                                      |
| `-c 'CMD'`       | run CMD and enable USDT probes on resulting process                            |
| `-q`             | 不打印运行过程的信息                                                           |
| `-v`             | 运行的详细信息                                                                 |
| `-k`             | bpf helper 返回一个错误信息的时候发出一个报警信息                              |
| `-kk`            | 检查所有 bpf helper 的函数                                                     |
| `--version`      | 输出 bpftrace 版本                                                             |

> bpftrace 脚本文件通常使用 `.bt` 作为文件的扩展名 

**示例**

```sh
# 查看版本
bpftrace --version
```

### `bpftrance -l`: 列出探针

> `bpftrace -l` 列出所有探针，并且可以添加搜索项

- 探针是用于捕获事件数据的检测点。
- 搜索词支持通配符，如 `*` 和 `?`。

**示例**

```sh
bpftrance -l

# 探针总数
bpftrace -l  | wc -l

# 各类探针数量
bpftrace -l  | awk -F ":" '{print $1}' | sort | uniq -c

# 使用通配符查询所有的系统调用跟踪点
bpftrace -l 'tracepoint:syscalls:*'
# 使用通配符查询所有名字包含"open"的跟踪点
bpftrace -l '*open*'

# 查询uprobe
bpftrace -l 'uprobe:/usr/lib/x86_64-linux-gnu/libc.so.6:*'

# 查询USDT
bpftrace -l 'usdt:/usr/lib/x86_64-linux-gnu/libc.so.6:*'

# 参数 -v 将会展示探针使用哪些参数，以供内置 args 变量使用
bpftrace -lv 'tracepoint:syscalls:sys_enter_open'
```

<!--
**探针分类**

- `kprobe` 内核调用事件
- `uprobe` 用户调用事件
- `tracepoint` 跟踪点
- `usdt` 用户态静态定义的跟踪器
- `kfunc` 内核函数跟踪器
- `profile/interval` 跟采样时间相关的设定
- `software` 软件执行层面的事件
- `hardware` 硬件事件
- `watchpoint` 内存监控点

-->

## 环境变量

## 探针

- `kprobe` - 内核函数调用事件
- `kretprobe` - 内核函数返回事件
- `uprobe` - 用户态函数调用事件
- `uretprobe` - 用户态函数返回事件
- `tracepoint` - kernel static tracepoints
- `usdt` - user-level static tracepoints
- `profile` - timed sampling
- `interval` - timed output
- `software` - 软件层面事件
- `hardware` - 硬件层面事件

一些指针类型允许通过模糊匹配获取多个指针，例如，`kprobe:vfs_*`。也可以指定多个具体名称的探针，使用逗号进行分隔。

### `kprobe`/`kretprobe`

**语法**

```
kprobe:function_name[+offset]
kretprobe:function_name
```

`kprobe` 用于函数执行之前开始，`kretprobe` 函数执行完成之后开始。

### `uprobe`/`uretprobe`

### `tracepoint`

**语法**

```
tracepoint:name
```

### `usdt`

**语法**

```
usdt:binary_path:probe_name
usdt:binary_path:[probe_namespace]:probe_name
usdt:library_path:probe_name
usdt:library_path:[probe_namespace]:probe_name
```

## 内置变量

| 变量                       | 说明                                                                                                  |
| -------------------------- | ----------------------------------------------------------------------------------------------------- |
| `pid`                      | Process ID (kernel tgid)                                                                              |
| `tid`                      | Thread ID (kernel pid)                                                                                |
| `uid`                      | User ID                                                                                               |
| `gid`                      | Group ID                                                                                              |
| `nsecs`                    | 时间戳，纳秒                                                                                          |
| `elapsed`                  | ebpfs 启动后的纳秒数                                                                                  |
| `numaid`                   | NUMA = Non-Uniform Memory Access，与多核 CPU 的内存访问相关                                           |
| `cpu`                      | 当前 cpu 编号，从 0 开始                                                                              |
| `comm`                     | 进程名称，通常为进程可执行文件名                                                                      |
| `kstack`                   | 内核栈                                                                                                |
| `ustack`                   | 用户栈                                                                                                |
| `args`                     | 所有参数构成的结构。`args` 参数只支持 tracepoint/kfunc/uprobeprobes，使用 `args.x` 获取具体参数的数据 |
| `arg0, arg1, ..., argN`    | 函数参数                                                                                              |
| `sarg0, sarg1, ..., sargN` | 函数参数(栈中)                                                                                        |
| `retval`                   | 返回值                                                                                                |
| `func`                     | 函数名，可以在可执行文件的符号表中这个函数名                                                          |
| `probe`                    | 探针的完整名称，也就是 bpftrace 中 形如 `kprobe:do_nanosleep`                                         |
| `curtask`                  | 当前 task struct                                                                                      |
| `rand`                     | 一个无符号 32 位随机数                                                                                |
| `cgroup`                   | 当前进程的 Cgroup，内核资源组，类似 namespace，docker 等虚拟化技术即基于内核提供的这一基础设施        |
| `cpid`                     | 子进程 pid，bpftrace 允许通过 `-c` 指定一个 cmd 运行，然后在该进程上安装 probe                        |
| `$1, $2, ..., $N, $#`      | bpftrace 程序自身的位置参数                                                                           |

## 内置函数

> bpftrace 无法定义函数，但提供了约 30 个内置函数，可以在 bpftrace 脚本的任意位置调用

**`printf(fmt,...)`**

**`print(...)`**

> 使用默认格式打印参数值

**`time(fmt)`**

> 打印当前时间，可以通过参数中的格式化字符串指定，如果没有指定格式化字符串，那么默认个时是 `%H:%M:%S\n`

| 格式化字符 | 说明                      |
| ---------- | ------------------------- |
| `%S`       | 秒，`00-60`               |
| `%M`       | 分钟，`00-59`             |
| `%I`       | 小时，`01-12`             |
| `%H`       | 小时，`00-23`             |
| `%d`       | 每月的第几天，`01-31`     |
| `%w`       | 星期，`0-6`, `0` 指星期日 |
| `%m`       | 月份，`01-12`             |
| `%y`       | 年份，`00-99`             |
| `%Y`       | 完整的年份                |

**`strftime(char *format, int nsecs)`**

> 返回一个格式化的时间戳

**`str(char *s [, int length])`**

**`join(char *arr[] [, char *delim])`**

> 打印数组，默认分隔符是空格

**`system(char *fmt)`**

> 执行 shell 命令

**`exit()`**

> 退出 bpftrace

**`cat(char *filename)`**

> 打印文件内容

## Map 函数

> Maps 是 BPF 的一种特殊的数据类型，被用来存储 counts， statistics 和 histograms；使用 `@` 修饰的变量也会被存储在 Maps 中。
>
> 当 bpftrace 退出时，所有的 Maps 将会被打印出来。

**`count()`**

> 计数当前函数被调用的次数