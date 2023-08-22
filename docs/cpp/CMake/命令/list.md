# `list`

> list 通过第一个参数设置需要进行的操作

## 将元素追加到列表中

```cmake
list(APPEND <list> [<element> ...])
```

## 在指定位置插入元素

```cmake
list(INSERT <list> <element_index> <element> [<element> ...])
```

## 在列表头追加元素

```cmake
list(PREPEND <list> [<element> ...])
```

## 将指定的元素从列表中移除

```cmake
list(REMOVE_ITEM <list> <value> [<value> ...])
```

## 将指定索引位置的元素从列表中移除

```cmake
list(REMOVE_AT <list> <index> [<index> ...])
```

## 移除列表尾部的元素

```cmake
list(POP_BACK <list> [<out-var>...])
```

## 移除列表头的元素

```cmake
list(POP_FRONT <list> [<out-var>...])
```

## 移除列表中的重复元素

```cmake
list(REMOVE_DUPLICATES <list>)
```

## 获取指定长度的列表

```cmake
list(LENGTH <list> <output variable>)
```

## 读取列表中指定索引的元素

```cmake
list(GET <list> <element index> [<element index> ...] <output variable>)
```

## 将列表中的元素用连接符（字符串）连接起来组成一个字符串

```cmake
list(JOIN <list> <glue> <output variable>)
```

## 查找指定元素所在位置

```cmake
list(FIND <list> <value> <output variable>)
```

`<output variable>`：新创建的变量

- 如果列表 `<list>` 中存在 `<value>`，那么返回 `<value>` 在列表中的索引
- 如果未找到则返回 `-1`