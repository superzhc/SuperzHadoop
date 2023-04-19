# 镜像：`nifi`

## 拉取镜像

```shell
docker pull apache/nifi:1.15.1
```

## 启动镜像

```shell
docker run --name nifi -p 8443:8443 -v /f/docker/volumes/nifi/nifi-current/logs:/opt/nifi/nifi-current/logs -e SINGLE_USER_CREDENTIALS_USERNAME=admin -e SINGLE_USER_CREDENTIALS_PASSWORD=admin0123456 -d apache/nifi:1.15.1
```

**注意**：密码必须最少12个字符，否则NIFI将生成随机的用户名和密码。

对于随机设置的用户名和密码可通过如下方式查找密码：

```
docker logs nifi | grep Generated
```