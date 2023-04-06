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
