package com.github.superzhc.groovy

import groovy.json.JsonSlurper
import org.junit.Test

class JsonTest {
    @Test
    void test() {
        String str = "{\"str\":\"xxxx\",\"age\":28,\"salary\":1234.567}"

        def parse = new JsonSlurper()
        def json = parse.parseText(str)
        println(json.age)
    }
}
