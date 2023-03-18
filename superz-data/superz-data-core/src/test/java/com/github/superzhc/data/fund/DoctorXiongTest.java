package com.github.superzhc.data.fund;

import com.github.superzhc.common.utils.MapUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DoctorXiongTest {

    @Test
    public void testFunds(){
        List<Map<String,Object>> data=DoctorXiong.funds("000478", "519671");
        System.out.println(MapUtils.print(data));
    }
}