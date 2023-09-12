# read-book-status-bar

## 配置

## \[√\] 1

```json
{
    "catalogUrl": "{list}",
    "contentUrl": "{list}{content}",
    "name": "aaa",
    "page": true,
    "parseCatalog": {
        "content": "",
        "list": "dd a",
        "url": ":href"
    },
    "parseContent": {
        "content": "#content"
    },
    "parseSearch": {
        "content": ".s2",
        "list": "#main li",
        "url": ".s2 a:href"
    },
    "searchUrl": "/modules/article/waps.php?keyword={name}",
    "searchUrlChartSet": "gbk",
    "url": "http://www.biqu520.net"
}
```