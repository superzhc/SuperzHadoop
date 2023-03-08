# 镜像：`minio`

## 拉取镜像

```bash
docker pull minio/minio:RELEASE.2023-02-22T18-23-45Z
```

## 启动镜像

<!--
```bash
docker run --name minio -p 9000:9000 -d minio/minio:RELEASE.2023-02-22T18-23-45Z server /data
```

这种安装方式并没有指定 ACCESS_KEY 和 SECRET_KEY 进行设置，安装后可以进行 echo 命令设置:

```bash
echo "admin" | docker secret create access_key -
echo "admin" | docker secret create secret_key -
```

**自定义用户的密钥**
-->

```bash
docker run -p 9000:9000 -p 9001:9001 --name minio -d -e "MINIO_ROOT_USER=admin" -e "MINIO_ROOT_PASSWORD=admin123456" -v /f/docker/volumes/minio/data:/data -v /f/docker/volumes/minio/config:/root/.minio minio/minio:RELEASE.2023-02-22T18-23-45Z server /data --console-address ":9001"
```

*注意*：密码不能设置过于简单，不然会报错

## 验证

**登录客户端**

```
http://127.0.0.1:9001
```