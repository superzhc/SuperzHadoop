package com.github.superzhc.data.news;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class Jin10Test {

    @Test
    public void news() {
        List<Map<String, Object>> data = Jin10.news();
        System.out.println(MapUtils.print(data));
    }
}