# 镜像：`httpd`

## 拉取镜像

```bash
docker pull httpd
```

## 启动镜像

**获取 httpd.conf 容器默认配置**

```bash
docker run --rm httpd cat /usr/local/apache2/conf/httpd.conf > D://docker/volumes/apache/conf/httpd.conf
```

**绑定数据卷并启动镜像**

```bash
docker run  --name httpd -p 8132:80 -v /d/docker/volumes/apache/www/:/usr/local/apache2/htdocs/ -v /d/docker/volumes/apache/conf/httpd.conf:/usr/local/apache2/conf/httpd.conf -v /d/docker/volumes/apache/logs/:/usr/local/apache2/logs/ -d httpd
```

## FAQ

### 文件目录下中文乱码

在 `httpd.conf` 文件中直接添加一行 `IndexOptions +Charset=UTF-8` 即可。