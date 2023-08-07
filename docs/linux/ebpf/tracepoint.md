# tracepoint

> tracepoint 是 Linux 内核预先定义的静态探测点，它分布于内核的各个子系统中。

## tracepoint 列表

```sh
ls /sys/kernel/debug/tracing/events/
```

系统中定义的 tracepoint 都在该目录下。

## tracepoint 数据格式

每个 tracepoint 都会按照已定义的数据格式来输出信息，可以在用户态查看 tracepoint 记录的内容格式，如：

```sh
cat /sys/kernel/debug/tracing/events/syscalls/sys_enter_open/format
name: sys_enter_open
ID: 614
format:
        field:unsigned short common_type;       offset:0;       size:2; signed:0;
        field:unsigned char common_flags;       offset:2;       size:1; signed:0;
        field:unsigned char common_preempt_count;       offset:3;       size:1; signed:0;
        field:int common_pid;   offset:4;       size:4; signed:1;

        field:int __syscall_nr; offset:8;       size:4; signed:1;
        field:const char * filename;    offset:16;      size:8; signed:0;
        field:int flags;        offset:24;      size:8; signed:0;
        field:umode_t mode;     offset:32;      size:8; signed:0;

print fmt: "filename: 0x%08lx, flags: 0x%08lx, mode: 0x%08lx", ((unsigned long)(REC->filename)), ((unsigned long)(REC->flags)), ((unsigned long)(REC->mode))
```

格式信息可以用来解析二进制的 trace 流数据。格式信息中包括两部分内容，第一部分是通用的格式，这类通用字段都带有 `common` 前缀，这是所有的 tracepoint event 都具备的字段。第二部分就是各个 tracepoint 所自定义的格式字段了，比如示例中的 nr，filename 等等。格式信息的最后一列是 tracepoint 的打印内容格式，通过这个可以看到打印数据的字段来源。