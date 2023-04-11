package com.github.superzhc.groovy.base

import org.junit.Test

/**
 * Groovy具有特殊的规则（通常称为Groovy Truth），用于将非布尔对象强制为布尔类型。
 *
 * 1. 布尔表达式
 * 2. 非空的集合和数组为真
 * 3. 如果匹配器（Matcher）至少有一个匹配，则为真
 * 4. 包含元素的迭代器和枚举被强制转换为真
 * 5. 非空映射被转换为真
 * 6. 非空的字符串、GString和CharSequences为真
 * 7. 非0数字为真
 * 8. 非空对象引用为真
 * 9. 为了自定义groovy是将对象转换为true还是false，可实现asBoolean()方法
 */
class GroovyTruthTest {

    @Test
    void testRule3() {
        assert ('a' =~ /a/)
        assert !('a' =~ /b/)
    }

    @Test
    void testRule5() {
        assert ["k": "v"]
        assert ![:]
    }
}
