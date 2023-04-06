package com.github.superzhc.groovy.base

import org.junit.Test

class ForInTest {
    @Test
    void list() {
        def lst = [1, 2, 3, 4, 5]

        for (item in lst) {
            println(item)
        }
    }

    @Test
    void range() {
        for (i in 1..10) {
            println(i)
        }
    }

    @Test
    void map() {
        def map = ["Ken": 21, "John": 25, "Sally": 22]

        for (entry in map) {
            println(entry)
        }
    }
}
