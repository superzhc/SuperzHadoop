# 配置

## 端口配置

| 功能                             | 属性                                  | 默认值 |
| -------------------------------- | ------------------------------------- | ------ |
| HTTPS Port                       | `nifi.web.https.port`                 | 8443   |
| Remote Input Socket Port         | `nifi.remote.input.socket.port`       | 10443  |
| Cluster Node Protocol Port       | `nifi.cluster.node.protocol.port`     | 11443  |
| Cluster Node Load Balancing Port | `nifi.cluster.node.load.balance.port` | 6342   |
| Web HTTP Forwarding Port         | `nifi.web.http.port.forwarding`       |        |

## HTTPS 访问

通过设置 `nifi.web.https.host` 和 `nifi.web.https.port` 属性来配置 HTTPS 访问 NiFi。`nifi.web.https.host` 属性指示服务器应在哪个主机名上运行。如果希望可以从所有网络接口访问 HTTPS 的 NiFi，则应使用 `0.0.0.0` 值。为了允许管理员将应用程序配置为仅在特定的网络接口上运行，可以指定 `nifi.web.http.network.interface*` 或 `nifi.web.https.network.interface*` 属性。

> 启用 HTTPS 时必须要取消设置 `nifi.web.http.port` 属性。NiFi 仅支持在 HTTP 或 HTTPS 上运行，而不是同时两种都支持。

