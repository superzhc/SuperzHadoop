# 镜像：`mongo`

## 拉取镜像

```bash
docker pull mongo:4.4.12-rc1
```

## 启动镜像

```bash
# --auth 需要密码才能访问mongo 
docker run -d --name mongodb -p 27017:27017 -v /d/docker/volumes/mongo/data:/data/db mongo:4.4.12-rc1 --auth
```

**设置用户和密码**

```bash
# 进入容器
docker exec -it mongo mongo admin

# 创建一个名为 admin，密码为 123456 的用户。
>  db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'},"readWriteAnyDatabase"]});
# 尝试使用上面创建的用户信息进行连接。
> db.auth('admin', '123456')
```