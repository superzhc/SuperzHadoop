package com.github.superzhc.groovy.base

import org.junit.Test

class IOTest {
    @Test
    void file() {
        def file = new File("E:\\downloads\\wordcount.txt")
        // File对象自身不带有eachLine，groovy使用了扩展方法来增强jdk
        file.eachLine { line -> println("line:${line}") }
        println("======================华丽分割线=====================")
        println(file.text)
    }
}
