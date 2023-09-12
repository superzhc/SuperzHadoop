# Json Schema

> 官网地址：<http://json-schema.org/>

JSON Schema 是基于 JSON 格式，用于定义 JSON 数据结构以及校验 JSON 数据内容。

## 语法

### `$schema`：声明遵循的版本规范

> 使用 `$schema` 关键字来声明使用的 Json Schema 规范版本

```
"$schema": "http://json-schema.org/draft-07/schema#"
"$schema": "https://json-schema.org/draft/2020-12/schema"
```

### \[draft 6\]`$id`：声明统一资源标识符

> `$id` 为 JSON Schema 声明一个统一资源标识符，使解析 `$ref` 时能够引用片段

注意：在 draft 4 及之前，声明统一资源标识符使用的关键字是 `id`

### `title`：定义标题

使用 `title` 关键字为 JSON Schema 文件提供标题

### `type`：定义元素类型

### `description`：提供描述信息

### \[draft 7\]`$comment`

## 数据类型

Json Schema 定义了以下基本类型：

| 类型    | 描述                                                |
| ------- | --------------------------------------------------- |
| string  | 字符串型，双引号包裹的 Unicode 字符和反斜杠转义字符 |
| number  | 数字型，包括整型(int)和浮点数型(float)              |
| boolean | 布尔型，true 或 false                               |
| object  | 对象型，无序的键值对集合                            |
| array   | 数组型，有序的值序列                                |
| null    | 空型                                                |


### `object`：对象类型

注意：在 JSON 中，key 必须是字符串类型

```json
{
    "type":"object",
    "properties": {
        "number": { "type": "number" }
    },
    "patternProperties": {
        "^S_": { "type": "string" },
        "^I_": { "type": "integer" }
    },
    "additionalProperties": false,
    "required": ["number"]
}
```

**`Properties`**

`properties` 关键字用于定义对象内的属性，`properties` 内是一个对象，每一个 key 是定义的对象的名称，每一个 value 是对应定义属性的校验规则。此关键字忽略未在其内定义的属性的校验

**`Pattern Properties`**

**`Additional Properties`**

用于控制是否允许未在 `properties` 和 `patternProperties` 中定义的属性，默认是允许的。

**`Required Properties`**

默认情况下 `properties` 关键字中定义的属性是非必须的，`required` 关键字中定义一个列表来定义必须的关键字。

### `array`:数组类型

```json
{
    "type":"array"
}
```

### `number`：数值类型

**integer**

```json
{
    "type":"integer"
}
```

**number**

```json
{
    "type": "number"
}
```

### `string`：字符串类型

```json
{
    "type":"string",
    "minLength": 2,
    "maxLength": 100,
    "pattern": "^(\\([0-9]{3}\\))?[0-9]{3}-[0-9]{4}$"
}
```

### `boolean`：布尔值类型

```json
{
    "type":"boolean"
}
```