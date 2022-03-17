# 镜像：`Sql Server`

## 拉取镜像

```bash
docker pull mcr.microsoft.com/mssql/server
```

## 启动容器

```bash
docker run --name=sqlserver -v /d/docker/volumes/sqlserver/data:/var/opt/mssql/data -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=superz1@3456' -h sqlserver -p 11433:1433  -d mcr.microsoft.com/mssql/server
```

**注意事项**：

1. 密码应符合 SQL Server 默认密码策略，否则容器无法设置 SQL Server，将停止工作。 默认情况下，密码的长度必须至少为 8 个字符，并且必须包含以下四种字符中的三种：大写字母、小写字母、十进制数字和符号。
2. Windows 上不能直接全部映射 `/var/opt/mssql`，这样会直接起不来sqlserver，[详细说明](https://docs.microsoft.com/zh-cn/sql/linux/tutorial-restore-backup-in-sql-server-container?view=sql-server-ver15)