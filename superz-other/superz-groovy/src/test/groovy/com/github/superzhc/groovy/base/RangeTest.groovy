package com.github.superzhc.groovy.base

import org.junit.Test

/**
 * 范围运算符
 */
class RangeTest {
    @Test
    void testAsc() {
        def range = 1..5

        println(range)
        println(range.get(2))
    }

    /**
     * 降序排列
     */
    @Test
    void testDesc() {
        def range = 5..1
        for (i in range) {
            println(i)
        }
    }

    @Test
    void testExclude() {
        def range = 1..<5
        for (i in range) {
            println(i)
        }
    }

    @Test
    void testChar() {
        def range = 'a'..'z'
        for (c in range) {
            println(c)
        }
    }

    @Test
    void switchSupportRange() {
        def age = 25;
        switch (age) {
            case 0..17:
                println '未成年'
                break
            case 18..30:
                println '青年'
                break
            case 31..50:
                println '中年'
                break
            default:
                println '老年'
        }
    }
}
