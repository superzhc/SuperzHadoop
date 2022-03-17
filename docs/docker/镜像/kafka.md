# 镜像：`Kafka`

## 拉取镜像

```bash
docker pull bitnami/kafka:2.2.1
```

## 启动镜像

```bash
docker run -d --name kafka -v /d/docker/volumes/kafka:/bitnami/kafka ^
-p 19092:9092 ^
-e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181/kafka --link zookeeper ^
-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092 ^
-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://10.50.60.147:19092 ^
-e ALLOW_PLAINTEXT_LISTENER=yes ^
bitnami/kafka:2.2.1
```

> **注意**：上述方式配置的 Kafka 必须配置 `advertised.listeners` 宿主机的 ip，不然会直接使用 docker 容器的 ip。