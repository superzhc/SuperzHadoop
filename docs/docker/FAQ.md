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

## [Windows] `ext4.vhxd` 占用的空间太大

执行如下命令进行优化：

```shell
optimize-vhd -Path "F:\\docker\\wsl\\docker-desktop-data\\ext4.vhdx" -Mode Full
```

**注意**：管理员权限下执行