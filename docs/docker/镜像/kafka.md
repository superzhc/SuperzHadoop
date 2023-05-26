# Kafka

## 镜像：`Kafka`

### 拉取镜像

```bash
docker pull bitnami/kafka:2.2.1
```

### 启动镜像

```bash
docker run -d --name kafka -v /d/docker/volumes/kafka/data:/bitnami/kafka/data ^
-p 9092:9092 ^
--network all ^
-e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181/kafka ^
-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092 ^
-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092 ^
-e ALLOW_PLAINTEXT_LISTENER=yes ^
bitnami/kafka:2.2.1
```

> **注意**：上述方式配置的 Kafka 必须配置 `advertised.listeners` 宿主机的 ip，不然会直接使用 docker 容器的 ip。
> 
> 若未配置成宿主机 ip，需要修改 host，采用此种模式，即上面启动方式的 host 配置为 `127.0.0.1 kafka`

#### 可配置环境变量

```shell
[2021-04-19 23:49:11,660] INFO KafkaConfig values:
	advertised.host.name = null
	advertised.listeners = PLAINTEXT://127.0.0.1:9092
	advertised.port = null
	authorizer.class.name =
	auto.create.topics.enable = true
	auto.leader.rebalance.enable = true
	background.threads = 10
	broker.id = 0
	broker.id.generation.enable = true
	broker.rack = null
	compression.type = producer
	connections.max.idle.ms = 600000
	controlled.shutdown.enable = true
	controlled.shutdown.max.retries = 3
	controlled.shutdown.retry.backoff.ms = 5000
	controller.socket.timeout.ms = 30000
	create.topic.policy.class.name = null
	default.replication.factor = 1
	delete.topic.enable = false
	fetch.purgatory.purge.interval.requests = 1000
	group.max.session.timeout.ms = 300000
	group.min.session.timeout.ms = 6000
	host.name =
	inter.broker.listener.name = null
	inter.broker.protocol.version = 0.10.2-IV0
	leader.imbalance.check.interval.seconds = 300
	leader.imbalance.per.broker.percentage = 10
	listener.security.protocol.map = SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,TRACE:TRACE,SASL_SSL:SASL_SSL,PLAINTEXT:PLAINTEXT
	listeners = PLAINTEXT://0.0.0.0:9092
	log.cleaner.backoff.ms = 15000
	log.cleaner.dedupe.buffer.size = 134217728
	log.cleaner.delete.retention.ms = 86400000
	log.cleaner.enable = true
	log.cleaner.io.buffer.load.factor = 0.9
	log.cleaner.io.buffer.size = 524288
	log.cleaner.io.max.bytes.per.second = 1.7976931348623157E308
	log.cleaner.min.cleanable.ratio = 0.5
	log.cleaner.min.compaction.lag.ms = 0
	log.cleaner.threads = 1
	log.cleanup.policy = [delete]
	log.dir = /tmp/kafka-logs
	log.dirs = /kafka/kafka-logs-2bfafd8a90d1
	log.flush.interval.messages = 9223372036854775807
	log.flush.interval.ms = null
	log.flush.offset.checkpoint.interval.ms = 60000
	log.flush.scheduler.interval.ms = 9223372036854775807
	log.index.interval.bytes = 4096
	log.index.size.max.bytes = 10485760
	log.message.format.version = 0.10.2-IV0
	log.message.timestamp.difference.max.ms = 9223372036854775807
	log.message.timestamp.type = CreateTime
	log.preallocate = false
	log.retention.bytes = -1
	log.retention.check.interval.ms = 300000
	log.retention.hours = 168
	log.retention.minutes = null
	log.retention.ms = null
	log.roll.hours = 168
	log.roll.jitter.hours = 0
	log.roll.jitter.ms = null
	log.roll.ms = null
	log.segment.bytes = 1073741824
	log.segment.delete.delay.ms = 60000
	max.connections.per.ip = 2147483647
	max.connections.per.ip.overrides =
	message.max.bytes = 1000012
	metric.reporters = []
	metrics.num.samples = 2
	metrics.recording.level = INFO
	metrics.sample.window.ms = 30000
	min.insync.replicas = 1
	num.io.threads = 8
	num.network.threads = 3
	num.partitions = 1
	num.recovery.threads.per.data.dir = 1
	num.replica.fetchers = 1
	offset.metadata.max.bytes = 4096
	offsets.commit.required.acks = -1
	offsets.commit.timeout.ms = 5000
	offsets.load.buffer.size = 5242880
	offsets.retention.check.interval.ms = 600000
	offsets.retention.minutes = 1440
	offsets.topic.compression.codec = 0
	offsets.topic.num.partitions = 50
	offsets.topic.replication.factor = 3
	offsets.topic.segment.bytes = 104857600
	port = 9092
	principal.builder.class = class org.apache.kafka.common.security.auth.DefaultPrincipalBuilder
	producer.purgatory.purge.interval.requests = 1000
	queued.max.requests = 500
	quota.consumer.default = 9223372036854775807
	quota.producer.default = 9223372036854775807
	quota.window.num = 11
	quota.window.size.seconds = 1
	replica.fetch.backoff.ms = 1000
	replica.fetch.max.bytes = 1048576
	replica.fetch.min.bytes = 1
	replica.fetch.response.max.bytes = 10485760
	replica.fetch.wait.max.ms = 500
	replica.high.watermark.checkpoint.interval.ms = 5000
	replica.lag.time.max.ms = 10000
	replica.socket.receive.buffer.bytes = 65536
	replica.socket.timeout.ms = 30000
	replication.quota.window.num = 11
	replication.quota.window.size.seconds = 1
	request.timeout.ms = 30000
	reserved.broker.max.id = 1000
	sasl.enabled.mechanisms = [GSSAPI]
	sasl.kerberos.kinit.cmd = /usr/bin/kinit
	sasl.kerberos.min.time.before.relogin = 60000
	sasl.kerberos.principal.to.local.rules = [DEFAULT]
	sasl.kerberos.service.name = null
	sasl.kerberos.ticket.renew.jitter = 0.05
	sasl.kerberos.ticket.renew.window.factor = 0.8
	sasl.mechanism.inter.broker.protocol = GSSAPI
	security.inter.broker.protocol = PLAINTEXT
	socket.receive.buffer.bytes = 102400
	socket.request.max.bytes = 104857600
	socket.send.buffer.bytes = 102400
	ssl.cipher.suites = null
	ssl.client.auth = none
	ssl.enabled.protocols = [TLSv1.2, TLSv1.1, TLSv1]
	ssl.endpoint.identification.algorithm = null
	ssl.key.password = null
	ssl.keymanager.algorithm = SunX509
	ssl.keystore.location = null
	ssl.keystore.password = null
	ssl.keystore.type = JKS
	ssl.protocol = TLS
	ssl.provider = null
	ssl.secure.random.implementation = null
	ssl.trustmanager.algorithm = PKIX
	ssl.truststore.location = null
	ssl.truststore.password = null
	ssl.truststore.type = JKS
	unclean.leader.election.enable = true
	zookeeper.connect = 127.0.0.1:2181
	zookeeper.connection.timeout.ms = 6000
	zookeeper.session.timeout.ms = 6000
	zookeeper.set.acl = false
	zookeeper.sync.time.ms = 2000
```

## 镜像：`schema-registry`

### 拉取镜像

```shell
docker pull bitnami/schema-registry:7.2.5
```

### 启动镜像

> 详细参数配置见：<https://hub.docker.com/r/bitnami/schema-registry>

```shell
docker run -d --name schema-registry -e SCHEMA_REGISTRY_ADVERTISED_HOSTNAME=zookeeper -e SCHEMA_REGISTRY_KAFKA_BROKERS=PLAINTEXT://kafka:9092 -e SCHEMA_REGISTRY_DEBUG=true -p 8081:8081 --network all --volume /d/docker/volumes/schema-registry/schema-registry-persistence:/bitnami bitnami/schema-registry:7.2.5
```

## 镜像：`kafka-ui`

### 拉取镜像

```shell
docker pull provectuslabs/kafka-ui
```

### 启动镜像

```shell
docker run -d --name kafka-ui --network all -p 6010:6010 -e SERVER_PORT=6010 -v /d/docker/volumes/kafka-ui/config.yml:/etc/kafkaui/dynamic_config.yaml -e DYNAMIC_CONFIG_ENABLED=true  provectuslabs/kafka-ui
```

**初始化连接信息**

1. 挂载数据卷

```shell
-v /d/docker/volumes/kafka-ui/config.yml:/etc/kafkaui/dynamic_config.yaml
```

2. 配置文件内容

```shell

```

**配置 `Basic Authentication` 认证**

```shell
-e AUTH_TYPE="LOGIN_FORM" -e SPRING_SECURITY_USER_NAME=admin -e SPRING_SECURITY_USER_PASSWORD=admin
```

**配置权限**

> 注意：`Basic Authentication` 不支持 RBAC

1. 启动镜像添加环境变量：

```shell
-e SPRING_CONFIG_ADDITIONAL-LOCATION=/roles.yml -v /d/docker/volumes/kafka-ui/roles.yml:/roles.yml
```

2. 配置文件内容：

```yml
rbac:
  roles:
    - name: "admins"
      clusters:
        # FILL THIS
      subjects:
        # FILL THIS
      permissions:
        - resource: applicationconfig
          actions: all
      
        - resource: clusterconfig
          actions: all

        - resource: topic
          value: ".*"
          actions: all

        - resource: consumer
          value: ".*"
          actions: all

        - resource: schema
          value: ".*"
          actions: all

        - resource: connect
          value: ".*"
          actions: all

        - resource: ksql
          actions: all
          
        - resource: acl
		  value: ".*"
          actions: [ view, edit ]
```