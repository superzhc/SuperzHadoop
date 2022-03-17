# 镜像：`minio`

## 拉取镜像

```bash
docker pull minio/minio:RELEASE.2021-06-17T00-10-46Z
```

## 启动镜像

```bash
docker run --name minio -p 9000:9000 -d minio/minio:RELEASE.2021-06-17T00-10-46Z server /data
```

这种安装方式并没有指定 ACCESS_KEY 和 SECRET_KEY 进行设置，安装后可以进行 echo 命令设置:

```bash
echo "admin" | docker secret create access_key -
echo "admin" | docker secret create secret_key -
```

**自定义用户的密钥**

```bash
docker run -p 9000:9000 --name minio \
-d --restart=always \
-e "MINIO_ACCESS_KEY=admin" \
-e "MINIO_SECRET_KEY=admin123456" \
-v /home/data:/data \
-v /home/config:/root/.minio \
minio/minio:RELEASE.2021-06-17T00-10-46Z server /data
```

*注意*：密码不能设置过于简单，不然会报错

## 验证

**登录客户端**

```
http://127.0.0.1:9000
```