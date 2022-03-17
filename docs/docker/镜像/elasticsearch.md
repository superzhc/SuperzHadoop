# 镜像：`ElasticSearch`

## 拉取镜像

```bash
docker pull elasticsearch:6.8.23
```

## 启动镜像

```bash
# 9200：Web端口，与RESTful客户端通信
# 9300：为TCP端口，与Java做通信
# ES在Docker中的所有数据都存储在容器中的 /usr/share/elasticsearch/data
# ES在Docker中的所有配置都存储在容器中的 /usr/share/elasticsearch/config [报错？？？]
## -v /d/docker/volumes/elasticsearch/config:/usr/share/elasticsearch/config
docker run -d --name es6.8 -p 9200:9200 -p 9300:9300 -v /d/docker/volumes/elasticsearch/data:/usr/share/elasticsearch/data -e "discovery.type=single-node" elasticsearch:6.8.23
```

### 安装 ik 分词器

es 自带的分词器对中文分词不是很友好，所以我们下载开源的 IK 分词器来解决这个问题。首先进入到 plugins 目录中下载分词器，下载完成后然后解压，再重启 es 即可。

```bash
# 进入容器内进行如下操作
cd /usr/share/elasticsearch/plugins/
# 注意事项：需要对应相应的版本，查看相应地址：https://github.com/medcl/elasticsearch-analysis-ik/releases
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.23/elasticsearch-analysis-ik-6.8.23.zip

# 重启容器
```