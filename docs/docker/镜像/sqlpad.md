# 镜像：`sqlpad`

## 拉取镜像

```shell
docker pull sqlpad/sqlpad
```

## 启动镜像

```shell
docker run -d -p 3000:3000 -e SQLPAD_ADMIN=admin@sqlpad.com -e SQLPAD_ADMIN_PASSWORD=admin --name sqlpad sqlpad/sqlpad
```