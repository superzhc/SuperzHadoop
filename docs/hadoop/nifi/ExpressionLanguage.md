# EL(Expression Language)

## 表达式语法

NiFi 表达式语言始终以起始分隔符 `${` 开头并以结束分隔符 `}` 结束，在开始和结束分隔符之间是表达式本身的文本。

**首个实体**

首个实体可以是如下选项：

- 属性名称
- 函数（注意跟函数调用不同，不需要添加调用字符 `:`，即 `<函数>(<函数参数列表>)`）

**函数调用**

```
:<函数名>(<函数参数1>,<函数参数1>,...)
```

**多函数调用**

> 多个函数调用即第一个函数调用的返回值是第二函数调用的实体，第二个函数的返回值是第三个函数调用的实体，以此类推，最后一个函数的调用返回最终的结果

```
:<函数1>(<函数参数列表1>):<函数2>(<函数参数列表2>)...:<函数n>(<函数参数列表n>)
```

**转义**

> 如果属性中存在特殊字符，使用 *单引号* 或者 *双引号* 转义。
> 
> EL 允许单引号和双引号互相转换，即转义属性 `my attribute` 可使用 `${"my attribute"}` 或者 `${'my attribute'}`。

需进行转义的规则：

- 属性名称的第一个字符是数字，需要进行转义
- 属性名称中包含特殊字符，需要进行转义

**特殊字符**

- `$` (美元符号)
- `|` (管道符)
- `{` (大括号开始符)
- `}` (大括号结束符)
- `(` (小括号开始符)
- `)` (小括号结束符)
- `[` (中括号开始符)
- `]` (中括号结束符)
- `,` (逗号)
- `:` (冒号)
- `;` (分号)
- `/` (斜杠)
- `*` (星号)
- `'` (单引号)
- ` `(空格)
- `\t` (制表符)
- `\r` (回车符)
- `\n` (换行符)

## EL 搜索属性的优先级

1. 在 FlowFile 中搜索属性
2. 搜索属性的进程组变量
3. 在文件注册表文件中搜索属性
4. 在 NiFi JVM 属性中搜索属性
5. 在系统环境变量中搜索属性

## 转义表达式

> 在表达式之前使用额外的 `$` 对表达式进行转义，即 `$${...}`

**示例**

|    表达式    |    值     | 备注                                                                                                            |
| :----------: | :-------: | --------------------------------------------------------------------------------------------------------------- |
|   `${abc}`   |   `xyz`   |                                                                                                                 |
|  `$${abc}`   | `${abc}`  |                                                                                                                 |
|  `$$${abc}`  |  `$xyz`   |                                                                                                                 |
| `$$$${abc}`  | `$${abc}` |                                                                                                                 |
| `$$$$${abc}` |  `$$xyz`  |                                                                                                                 |
|     `$5`     |   `$5`    | 这里没有实际的表达式                                                                                            |
|    `$$5`     |   `$$5`   | `$` 字符不会被转义，因为它不是紧跟在表达式之前                                                                  |
|   `$$${5`    |  `$$${5`  | 因为这里没有右大括号，所以没有实际的表达式，因此 `$` 字符不会被转义                                             |
|   `$$${5}`   | `<Error>` | 此表达式无效，因为它等于转义的 `$`，后跟 `${5}`，并且 `${5}` 不是有效的表达式。这里数字必须转义才能表示成字符串 |
|  `$$${'5'}`  |    `$`    | 没有名为 `5` 的属性，因此表达式的计算结果为空字符串。 `$$` 评估为单个（转义）`$`，因为它紧接在表达式之前        |

## 函数

### 数据类型

函数的每个参数和函数返回的每个值都具有特定的数据类型。表达式语言支持以下几种数据类型:

- String：字符串
- Number：Number 是由一个或多个数字组成的整数(0 到 9)
- Decimal：Decimal 是一个数值，可以支持小数和更大的值，而精度损失最小
- Date：Date 是一个包含日期和时间的对象
- Boolean：Boolean 布尔值是 true 或者 false

> 表达式语言函数计算后所得的最终值都存储为 String 类型。

### 函数使用

**`and(condition)`、`or(condition)`**

```
${filename:toLower():equals( ${filename} ):and(
	${filename:length():ge(5)}
)}

${filename:toLower():equals( ${filename} ):or(
	${filename:length():equals(5)}
)}
```

**`ifElse(EvaluateIfTrue,EvaluateIfFalse)`**

```
${bool:ifElse('a','b')}
结果：a

${literal(true):ifElse('a','b')}
结果：a

${nullFilename:isNull():ifElse('file does not exist', 'located file')}
结果：file does not exist

${nullFilename:ifElse('found', 'not_found')}
结果：not_found

${filename:ifElse('found', 'not_found')}
结果：not_found

${filename:isNull():not():ifElse('found', 'not_found')}
结果：found
```

**`equals(value)`、`equalsIgnoreCase(value)`**

> `equals(value)`：比较两个值是否相等
>
> `equalsIgnoreCase(value)`：比较两个值是否相等，忽略大小写

```
${filename:equals('hello.txt')}

${filename:equalsIgnoreCase('hello.txt')}
```

**`jsonPath(jsonPathExpression)`**

> jsonPath 函数通过将主题视为 JSON 并应用 JSON 路径表达式来生成字符串。如果主题不包含有效的JSON， jsonPath 无效，或者主题中不存在路径，都将生成空字符串。如果评估结果为标量值，则生成标量值的字符串表示。否则，将生成 JSON 结果的字符串。当 `[0]` 作为标量时，长度为1的JSON数组是特殊情况，会生成一个代表 `[0]` 的字符串。

```
${myJson:jsonPath('$.firstName')}
```

#### 时间函数

**`now()`**

> 获取当前时间

| Expression                                        | Value                |
| ------------------------------------------------- | -------------------- |
| `${now()}`                                        | 获取当前时间         |
| `${now():toNumber()}`                             | 获取当前时间的时间戳 |
| `${now():toNumber():minus(86400000)`              | 当前时间的前一天     |
| `${now():format('yyyy')}`                         | 获取今年             |
| `${now():toNumber():minus(86400000):format('E')}` |                      |

**`format(formatStr[,timeZone])`**

> 格式化时间，使用该函数的类型为 Number

- formatStr:使用 Java SimpleDateFormat 的语法进行格式化
- timeZone:【可选】使用 Java TimeZone 语法进行时区设置

| Expression                                                            | Value                      |
| --------------------------------------------------------------------- | -------------------------- |
| `${time:format("yyyy/MM/dd HH:mm:ss.SSS'Z'", "GMT")}`                 | `2014/12/31 20:36:03.264Z` |
| `${time:format("yyyy/MM/dd HH:mm:ss.SSS'Z'", "America/Los_Angeles")}` | `2014/12/31 12:36:03.264Z` |
| `${time:format("yyyy/MM/dd HH:mm:ss.SSS'Z'", "Asia/Tokyo")}`          | `2015/01/01 05:36:03.264Z` |
| `${time:format("yyyy/MM/dd", "GMT")}`                                 | `2014/12/31`               |
| `${time:format("HH:mm:ss.SSS'Z'", "GMT")}`                            | `20:36:03.264Z`            |
| `${time:format("yyyy", "GMT")}`                                       | `2014`                     |

#### 类型转换

**`toNumber()`**

> 将类型转换成 Number 类型，支持进行该函数转换的类型为：String, Decimal, or Date