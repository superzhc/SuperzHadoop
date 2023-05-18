# InvokeHTTP

> 该处理器用于执行HTTP请求

## 属性

### `* $ HTTP Method`

> HTTP 请求方法(GET、POST、PUT、PATCH、DELETE、HEAD、OPTIONS)。除了 POST、PUT 和 PATCH 之外的方法将在没有消息体的情况下发送。
> 
> 默认值：`GET`

支持表达式语言:true

### `* Remote URL`

> 将连接到的远程 URL，包括协议、IP、端口、路径。

### `SSL Context Service`

> SSL上下文服务，为 TLS/SSL (https) 连接提供客户端证书信息，还用于连接到 HTTPS 代理。

### `* Connection Timeout`

> 连接到远程服务的最大等待时间。默认值：`5 secs`

### `* Read Timeout`

> 远程服务响应的最大等待时间。默认值：`15 secs`

### `* Include Date Header`True

> 请求中是否包含一个 `RFC-2616` 日期头。

### `* Follow Redirects`True

> 是否遵循远程服务器发出的 HTTP 重定向。

### `Attributes to Send`

> 正则表达式，它定义在请求中哪些属性作为 HTTP 头发送。如果没有定义，则不会将属性作为 HTTP 头发送。此外，任何用户自定义的动态属性都将作为 HTTP 请求头发送。

### `Basic Authentication Username`

> 客户端用于远程 URL 进行身份验证的用户名。不能包含控制字符`(0-31)`、`':'` 或 `DEL(127)`。

### `Basic Authentication Password`

> 客户端用于远程 URL 进行身份验证的密码。

### `Proxy Configuration Service`

> 代理控制器服务。如果设置，它将取代该处理器配置的代理设置。支持的代理有:`SOCKS`, `HTTP + AuthN`

### `$ Proxy Host`

> 代理服务器的主机名或 IP 地址

### `$ Proxy Port`

> 代理服务器的端口

### `$ Proxy Type`

> http 代理类型。必须是 http 或者 https

### `$ Proxy Username`

> 针对代理进行身份验证时的用户名

### `$ Proxy Password`

> 针对代理进行身份验证时的密码

### `$ Put Response Body In Attribute`

> 如果设置了，返回的响应体将被放入原始流文件的属性中，而不是单独的流文件。要放入的属性键由该属性的值决定。

### `Max Length To Put In Attribute`

> 如果将响应体路由到原始属性(即设置了“Put response body in attribute”属性)，则向属性值中放入的字符最多为这个数量。这一点很重要，因为属性保存在内存中，而较大的属性将很快导致内存不足。如果输出比这个值长，则将其截断。如果可能的话，考虑将其缩小。
> 
> 默认值：`256`

### `Use Digest Authentication`

> 是否使用摘要身份验证与网站通信。`Basic Authentication Username` 和 `Basic Authentication Password` 用于认证。
> 
> 默认值：`false`

### `Always Output Response`

> 不管接收到的服务器状态代码是什么，将强制生成响应流文件并将其路由到“响应”关系中
> 
> 默认值：`false`

### `Trusted Hostname`

> 绕过普通的信任存储库主机名验证程序，以允许指定的远程主机名为可信主机。启用此属性具有 MITM 安全含义，请明智地使用。仍然接受基于普通信任库主机名验证器的其他连接。仅对 SSL (HTTPS) 连接有效。

### `Add Response Headers to Request`

> 启用此属性可将所有响应标头保存到原始请求中。
> 
> 默认值：`false`

### `* Content-Type`

> 指定通过 PUT、POST 或 PATCH 传输的内容类型。在对表达式语言表达式求值后出现空值的情况下，`Content-Type` 默认为`application/octe-stream`
> 
> 默认值：`${mime.type}`

### `Send Message Body`

> 如果为 true，则在 `POST/PUT/PATCH` 请求上发送 HTTP 消息体(默认)。如果为 false，则为这些请求禁用消息体和内容类型头部。
> 
> 默认值：`true`

### `* Use Chunked Encoding`

> 传送内容时将此属性设置为 true，目的是不传递 `content-length`，而是发送 `Transfer-Encoding`，其值为 `chunked`。这将使HTTP 1.1中引入的数据传输机制能够以块的形式传递未知长度的数据。
> 
> 默认值：`false`

### `Penalize on "No Retry"`

> 启用此属性将惩罚路由到“No Retry”关系的流文件。
> 
> 默认值：`false`

### `* Use HTTP ETag`

> 支持HTTP请求的HTTP实体标记(ETag)。
> 
> 默认值：`false`

### `* Maximum ETag Cache Size`

> 允许ETag缓存增长到的最大大小。默认大小为 `10MB`。