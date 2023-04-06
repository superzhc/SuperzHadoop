package com.github.superzhc.groovy.base

import org.junit.Test

class CollectionTest {
    @Test
    void list() {
        def l1 = [1, 1.0, Long.MAX_VALUE, "aaa", [2, 3]]
        println(l1)
    }

    @Test
    void map() {
        // 空映射
        def m1 = [:]
        def m2 = ['TopicName': 'Lists', 'TopicName': 'Maps']
        println(m2)
    }
}
