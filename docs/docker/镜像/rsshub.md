# RSSHub

官网：<https://docs.rsshub.app/install/#docker-jing-xiang>

## 拉取镜像

```sh
docker pull diygod/rsshub:chromium-bundled
```

> 如需启用 puppeteer，可使用 `diygod/rsshub:chromium-bundled`；若指定日期则为 `diygod/rsshub:chromium-bundled-2021-06-18`。

## 启动镜像

```sh
docker run -d --name rsshub -p 1200:1200 -e CACHE_EXPIRE=300 diygod/rsshub:chromium-bundled
```

### 配置

**网络配置**

- PORT: 监听端口，默认为 1200
- SOCKET: 监听 Unix Socket，默认 null
- LISTEN_INADDR_ANY: 是否允许公网连接，默认 1
- REQUEST_RETRY: 请求失败重试次数，默认 2
- REQUEST_TIMEOUT: 请求超时毫秒数，默认 3000
- UA: 用户代理，默认为随机用户代理用户代理（macOS 上的 Chrome）
- NO_RANDOM_UA: 是否禁用随机用户代理，默认 null

**缓存配置**

RSSHub 支持 memory 和 redis 两种缓存方式

- CACHE_TYPE: 缓存类型，可为 memory 和 redis，设为空可以禁止缓存，默认为 memory
- CACHE_EXPIRE: 路由缓存过期时间，单位为秒，默认 5 * 60
- CACHE_CONTENT_EXPIRE: 内容缓存过期时间，每次访问会重新计算过期时间，单位为秒，默认 1 * 60 * 60
- REDIS_URL: Redis 连接地址（redis 缓存类型时有效），默认为 redis://localhost:6379/
- MEMORY_MAX: 最大缓存数量（memory 缓存类型时有效），默认 256

## 停止容器

```sh
docker stop rsshub
```

## 更新

```sh
docker stop rsshub
docker rm rsshub

docker pull diygod/rsshub:chromium-bundled
# docker run -d --name rsshub -p 1200:1200 diygod/rsshub:chromium-bundled
```