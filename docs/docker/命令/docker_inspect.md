# `docker inspect`：查看 docker 对象的底层基础信息

docker inspect是docker客户端的原生命令，用于查看 docker 对象的底层基础信息。包括容器的 id、创建时间、运行状态、启动参数、目录挂载、网路配置等等。另外，该命令也可以用来查看 docker 镜像的信息。

官方描述：

> Return low-level information on Docker objects

## 语法

```bash
docker inspect [OPTIONS] NAME|ID [NAME|ID...]
```

**OPTIONS选项**

| Name, shorthand | Default   | Description                                       |
| --------------- | --------- | ------------------------------------------------- |
| --format , -f   |           | Format the output using the given Go template     |
| --size , -s     |           | 用于查看容器的文件大小                            |
| --type          | | 用于指定 docker 对象的类型，如：container、images |