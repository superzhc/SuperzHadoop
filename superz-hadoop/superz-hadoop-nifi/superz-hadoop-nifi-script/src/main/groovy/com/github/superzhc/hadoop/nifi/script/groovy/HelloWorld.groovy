package com.github.superzhc.hadoop.nifi.script.groovy

import com.github.superzhc.hadoop.nifi.script.GroovyEnvironmentTemplate

class HelloWorld extends GroovyEnvironmentTemplate {
    def test() {
        print("Hello World")
    }

    def demo() {
        def flowFile = session.get()
        if (null == flowFile) {
            return
        }
    }
}
