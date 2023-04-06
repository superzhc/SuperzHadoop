package com.github.superzhc.groovy.base

import org.junit.Test

/**
 * 范围运算符
 */
class RangeTest {
    @Test
    void test() {
        def range = 1..5

        println(range)
        println(range.get(2))
    }

    @Test
    void exclude() {
        def range = 1..<5
        for (i in range) {
            println(i)
        }
    }
}
