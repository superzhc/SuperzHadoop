package com.github.superzhc.groovy.base

import org.junit.Test

class OperatorTest {
    @Test
    // 三目运算符
    void testElvisOperator() {
        String name = null
        def displayName = name ? name : "superz"
        println(displayName)

        // 简洁三目运算符写法
        name = "superz2"
        displayName = name ?: "superz"
        println(displayName)
    }

    @Test
    // 展开运算符
    void testExpandOperator() {
        def lst = ["a", "b", "c"]
        // *。 运算符应用于实现了 iterator 的对象，对内部的对象进行了遍历操作
        def lst2 = lst*.toUpperCase()
        println(lst2)
    }

    @Test
    // 安全运算符
    void testSafeOperator() {
        List<String> lst = null
        // 用 ?. 可以在lst为null的时候调用函数不会报空指针的问题，直接返回null
        def index_2 = lst?.get(1)
        println(index_2)
    }
}
