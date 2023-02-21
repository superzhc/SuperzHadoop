# FAQ

## [Windows] `Ports are not available: listen tcp 0.0.0.0:7474: bind: An attempt was made to access a socket in a way forbidden by its access permissions.`

**端口占用问题，但经排查端口实际未被占用**

执行如下命令即可解决：

```sh
net stop winnat
docker start <容器名称>
net start winnat
```

> **注**：上述命令需要使用管理员权限