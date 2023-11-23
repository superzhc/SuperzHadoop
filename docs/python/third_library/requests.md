# Requests

> Github:<https://github.com/psf/requests>

## 安装

```sh
pip install requests
```

## requests 方法

| 方法                          | 描述                            |
| ----------------------------- | ------------------------------- |
| `get(url, params, args)`      | 发送 GET 请求到指定 url         |
| `head(url, args)`             | 发送 HEAD 请求到指定 url        |
| `patch(url, data, args)`      | 发送 PATCH 请求到指定 url       |
| `post(url, data, json, args)` | 发送 POST 请求到指定 url        |
| `put(url, data, args)`        | 发送 PUT 请求到指定 url         |
| `delete(url, args)`           | 发送 DELETE 请求到指定 url      |
| `request(method, url, args)`  | 向指定的 url 发送指定的请求方法 |
