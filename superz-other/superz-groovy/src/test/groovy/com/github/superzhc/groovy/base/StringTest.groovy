package com.github.superzhc.groovy.base

import org.junit.Test

class StringTest {

    void define() {
        def s1 = 'this is a content'
        def s2 = "this is a content"
        def s3 = "this is a " + "content"
        def s4 = """
this is a conent
this is a another content
"""
    }

    @Test
    void index() {
        def str = "this is a content"
        println(str[10])
        // 获取最后一个字符
        println(str[-1])
        println(str[0..3])
        println(str[3..0])
    }
}
