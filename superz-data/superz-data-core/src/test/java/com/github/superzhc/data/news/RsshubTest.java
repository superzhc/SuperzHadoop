package com.github.superzhc.data.news;

import com.github.superzhc.common.dom4j.XmlUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RsshubTest {

    Rsshub api;

    @Before
    public void setUp() throws Exception {
        api = new Rsshub("https://rsshub.app", "127.0.0.1", 10809);
    }

    @Test
    public void test() {
        List<Map<String, Object>> data = api.get("/21caijing/channel/readnumber");

        System.out.println(MapUtils.print(data));
    }
}