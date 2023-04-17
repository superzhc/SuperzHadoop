package com.github.superzhc.common.jackson;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/17 16:34
 **/
public class JsonTest {
    ObjectNode node;

    @Before
    public void setUp() {
        node = JsonUtils.mapper().createObjectNode();
    }

    @Test
    public void test() {
        JsonUtils.put(node, "key", new Object[]{new String[]{"aa", "bb"}, new int[]{11, 22}, "cc"}, "c1", "c2", "c3");
        System.out.println(JsonUtils.asString(node));
    }
}
