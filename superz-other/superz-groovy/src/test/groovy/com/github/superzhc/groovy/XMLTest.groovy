package com.github.superzhc.groovy

class XMLTest {
    void test() {
        def text = '''
    <list>
        <technology>
            <name>Groovy</name>
        </technology>
    </list>
'''

        def list = new XmlSlurper().parseText(text)
    }

    void test2() {
        def text = '''
    <list>
        <technology>
            <name>Groovy</name>
        </technology>
    </list>
'''

        def list = new XmlParser().parseText(text)
    }
}
