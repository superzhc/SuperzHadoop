package com.github.superzhc.data.news;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class AnyKnewTest {

    List<Map<String, String>> data = null;


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        MapUtils.show(data);
    }

    @Test
    public void testWeibo() {
        data = AnyKnew.weibo();
    }

    @Test
    public void testZhihu() {
        data = AnyKnew.zhihu();
    }
}
