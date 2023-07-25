# bpftrance

> bpftrace 通过高度抽象的封装来使用 eBPF，大多数功能只需要寥寥几笔就可以运行起来。

## **语法**

bpftrance 本质上是一种脚本语言，风格与 AWK 非常相似，脚本的结构：

```c
/* 采用与 C 语言类似的注释 */
​
BEGIN{
  // 开始时执行一次的代码
}
probe /filter/ {
  // 事件与 prob 和 filter 匹配时执行
}
END{
  // 结束时执行
}
```

## **执行方式**

执行方式有两种：

- `bpftrace -e 'cmds'`: 执行单行指令
- `bpftrace filename`: 执行脚本文件

## 内置变量

| 变量                       | 说明                                                                                           |
| -------------------------- | ---------------------------------------------------------------------------------------------- |
| `pid`                      | Process ID (kernel tgid)                                                                       |
| `tid`                      | Thread ID (kernel pid)                                                                         |
| `uid`                      | User ID                                                                                        |
| `gid`                      | Group ID                                                                                       |
| `nsecs`                    | 时间戳，纳秒                                                                                   |
| `elapsed`                  | ebpfs 启动后的纳秒数                                                                           |
| `numaid`                   | NUMA = Non-Uniform Memory Access，与多核 CPU 的内存访问相关                                    |
| `cpu`                      | 当前 cpu 编号，从 0 开始                                                                       |
| `comm`                     | 进程名称，通常为进程可执行文件名                                                               |
| `kstack`                   | 内核栈                                                                                         |
| `ustack`                   | 用户栈                                                                                         |
| `arg0, arg1, ..., argN`    | 函数参数                                                                                       |
| `sarg0, sarg1, ..., sargN` | 函数参数(栈中)                                                                                 |
| `retval`                   | 返回值                                                                                         |
| `func`                     | 函数名，可以在可执行文件的符号表中这个函数名                                                   |
| `probe`                    | 探针的完整名称，也就是 bpftrace 中 形如 `kprobe:do_nanosleep`                                  |
| `curtask`                  | 当前 task struct                                                                               |
| `rand`                     | 一个无符号 32 位随机数                                                                         |
| `cgroup`                   | 当前进程的 Cgroup，内核资源组，类似 namespace，docker 等虚拟化技术即基于内核提供的这一基础设施 |
| `cpid`                     | 子进程 pid，bpftrace 允许通过 `-c` 指定一个 cmd 运行，然后在该进程上安装 probe                 |
| `$1, $2, ..., $N, $#`      | bpftrace 程序自身的位置参数                                                                    |

## 内置函数

> bpftrace 无法定义函数，但提供了约 30 个内置函数，可以在 bpftrace 脚本的任意位置调用

**`printf(fmt,...)`**

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

## bpftrance 支持探针

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
```

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