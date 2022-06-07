# Linux

## 查看 Linux 操作系统

1. 使用 Linux 的 etc 目录下的 os-release 文件：`cat /etc/os-release`
2. 使用 lsb_release 命令：`lsb_release -a`；注意，不通用，Ubuntu 可用，CentOS 不可用
3. 使用 Linux 的 proc 目录下的可用版本文件：`cat /proc/version`