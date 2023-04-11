# SSH 隧道

## 本地端口转发

**语法**

```shell
ssh -L [local_bind_addr:]local_port:remote:remote_port middle_host
```

**示例**

```shell
ssh -g -L 2222:host2:80 host3
```

其中"-L"选项表示本地端口转发，其工作方式为：在本地指定一个由ssh监听的转发端口(2222)，将远程主机的端口(host2:80)映射为本地端口(2222)，当有主机连接本地映射端口(2222)时，本地ssh就将此端口的数据包转发给中间主机(host3)，然后host3再与远程主机的端口(host2:80)通信。

## 远程端口转发

**语法**

```shell
ssh -R <远程端口>:<目的地址>:<目的端口> <登陆用户>@<SSH服务器>
```