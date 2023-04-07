package com.github.superzhc.groovy.base

import org.junit.Test

class ClosureTest {

    @Test
    void calling() {
        def code = { 123 }
        println(code() == 123)
        println(code.call() == 123)
    }

    @Test
    void normalParameters() {
        def closureWithOneArg = { str -> str.toUpperCase() }
        assert closureWithOneArg('groovy') == 'GROOVY'

        def closureWithOneArgAndExplicitType = { String str -> str.toUpperCase() }
        assert closureWithOneArgAndExplicitType('groovy') == 'GROOVY'

        def closureWithTwoArgs = { a, b -> a + b }
        assert closureWithTwoArgs(1, 2) == 3

        def closureWithTwoArgsAndExplicitTypes = { int a, int b -> a + b }
        assert closureWithTwoArgsAndExplicitTypes(1, 2) == 3

        def closureWithTwoArgsAndOptionalTypes = { a, int b -> a + b }
        assert closureWithTwoArgsAndOptionalTypes(1, 2) == 3

        def closureWithTwoArgAndDefaultValue = { int a, int b = 2 -> a + b }
        assert closureWithTwoArgAndDefaultValue(1) == 3
    }

    @Test
    void implicitParameter() {
        def greeting = { "Hello, $it!" }
        assert greeting('Patrick') == 'Hello, Patrick!'
    }
}
