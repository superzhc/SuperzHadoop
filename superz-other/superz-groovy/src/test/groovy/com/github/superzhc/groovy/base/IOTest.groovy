package com.github.superzhc.groovy.base

import org.junit.Test

class IOTest {
    @Test
    void file() {
        def file = new File("E:\\downloads\\wordcount.txt")
        // File对象自身不带有eachLine，不知道为啥可以直接调用 TODO
        file.eachLine { line -> println("line:${line}") }
        println("======================华丽分割线=====================")
        println(file.text)
    }
}
