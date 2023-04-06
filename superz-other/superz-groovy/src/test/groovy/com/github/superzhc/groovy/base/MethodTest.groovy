package com.github.superzhc.groovy.base

import org.junit.Test

class MethodTest {
    //===============================默认参数===============================================
    /**
     * Groovy 中还有一个规定来指定方法中的参数的默认值。 如果没有值传递给参数的方法，则使用缺省值。
     * 如果使用非默认和默认参数，则必须注意，默认参数应在参数列表的末尾定义。
     */
    def defaultParamMethod(p1, p2 = "superz", p3 = Double.MAX_VALUE) {
        println("[${p1}] ${p2}=${p3}")
    }

    @Test
    void testDefaultParamMethod() {
        defaultParamMethod("LOG")
        defaultParamMethod("LOG", "Y")
        // [[p3:4.9E-324]] LOG=1.7976931348623157E308；非想要的结果， FIXME
        defaultParamMethod("LOG", p3: Double.MIN_VALUE)
    }
    //===============================默认参数===============================================
}
