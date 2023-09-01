# JsonPath

|            操作符            | 说明                                                     |
| :--------------------------: | -------------------------------------------------------- |
|             `$`              | 表示文档的根元素                                         |
|             `@`              | 表示文档的当前元素                                       |
| `.node_name`/`['node_name']` | 匹配下级节点                                             |
|          `[index]`           | 检索数组中的元素，JsonPath 数组的索引从 0 开始           |
|            `[,]`             | 连接操作符，将多个结果拼接成数组返回，可以使用索引或别名 |
|      `[start:end:step]`      | 支持数组切片语法                                         |
|             `*`              | 作为通配符，匹配所有成员                                 |
|             `..`             | 子递归通配符，匹配成员的所有子元素                       |
|          `(<expr>)`          | 使用表达式                                               |
|     `?(<boolean expr>)`      | 进行数据筛选                                             |

**过滤器**

| 操作符  | 说明                                                      |
| :-----: | --------------------------------------------------------- |
|  `==`   | 等于                                                      |
|  `!=`   | 不等于                                                    |
|   `<`   | 小于                                                      |
|  `<=`   | 小于等于                                                  |
|   `>`   | 大于                                                      |
|  `>=`   | 大于等于                                                  |
|  `=~`   | 判断是否符合正则表达式，例如 `[?(@.name =~ /foo.*?/i)]`   |
|  `in`   | 包含                                                      |
|  `nin`  | 不包含                                                    |
| `size`  | 左边数组/字符串的长度是否等于右边的值，`?(@.book size 4)` |
| `empty` | 判断是否为空                                              |

## 示例

**示例 Json**

```json
{
    "store": {
        "book": [{
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            }, {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            }, {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            }, {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "author": "张三",
            "color": "red",
            "price": 19.95
        },
        "author": "sss"
    },
    "author": "superz"
}
```

| 表达式                   | 结果                                     |
| ------------------------ | ---------------------------------------- |
| `$.store.book[*].author` | 所有book的author节点                     |
| `$..author`              | 所有author节点                           |
| `$.store.*`              | store下的所有节点，book数组和bicycle节点 |
| `$.store..price`         | store下的所有price节点                   |
| `$..book[2]`             | 匹配第3个book节点                        |
| `$..book[(@.length-1)]`  | 匹配倒数第1个book节点                    |
| `$..book[-1:]`           | 匹配倒数第1个book节点                    |
| `$..book[0,1]`           | 匹配前两个book节点                       |
| `$..book[:2]`            | 匹配前两个book节点                       |
| `$..book[?(@.isbn)]`     | 过滤含isbn字段的节点                     |
| `$..book[?(@.price<10)]` | 过滤 `price<10` 的节点                   |
| `$..*`                   | 递归匹配所有子节点                       |