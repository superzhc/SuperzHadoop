# Hadoop 安装部署

## 创建 Hadoop 用户

```shell
useradd hadoop
# 设置密码
passwd hadoop

usermod -g hadoop hadoop
```

## 设置 Root 权限

编辑 `/root/sudo` 文件：

```shell
hadoop ALL=(ALL)NOPASSWD:ALL
```