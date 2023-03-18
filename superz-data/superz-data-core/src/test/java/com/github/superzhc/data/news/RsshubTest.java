package com.github.superzhc.data.news;

import com.github.superzhc.common.dom4j.XmlUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RsshubTest {

    Rsshub api;

    Map<String, Object> commonParams;

    @Before
    public void setUp() throws Exception {
//        api = new Rsshub("https://rsshub.app", "127.0.0.1", 10809);
        api = new Rsshub("http://127.0.0.1:1200");

        commonParams = new HashMap<>();
        commonParams.put("limit", 50);
    }

    private void show(String path) {
        show(path, commonParams);
    }

    private void show(String path, Map<String, Object> params) {
        List<Map<String, Object>> data = api.get(path, params);
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void testGet() {
        List<Map<String, Object>> data = api.get("/21caijing/channel/readnumber");

        System.out.println(MapUtils.print(data));
    }

    @Test
    public void testGetJson() {
        List<Map<String, Object>> data = api.getJson("/21caijing/channel/readnumber.json");

        System.out.println(MapUtils.print(data));
    }

    @Test
    public void douban_book_latest() {
        List<Map<String, Object>> data = api.get("/douban/book/latest", commonParams);
        System.out.println(MapUtils.print(data));
    }

    @Test
    public void ke_researchResults() {
        show("/ke/researchResults", commonParams);
    }

    @Test
    public void kpmg_insights() {
        show("kpmg/insights", commonParams);
    }

    @Test
    public void guduodata_daily() {
        show("guduodata/daily", commonParams);
    }

    @Test
    public void qiyoujiage() {
        show("/qiyoujiage/jiangsu/nanjing", commonParams);
    }

    @Test
    public void aicaijing_latest() {
        show("/aicaijing/latest");
    }

    @Test
    public void aicaijing_cover(){
        show("/aicaijing/cover");
    }

    @Test
    public void aicaijing_recommend(){
        show("/aicaijing/recommend");
    }

    @Test
    public void aicaijing_information(){
        show("aicaijing/information/14");
    }

    @Test
    public void bigquant_collections(){
        show("bigquant/collections");
    }

    @Test
    public void dtcj_datainsight(){
        show("/dtcj/datainsight");
    }

    @Test
    public void dtcj_datahero(){
        show("/dtcj/datahero");
    }

    @Test
    public void weexcn_news(){
        show("/weexcn/news/1");
    }

    @Test
    public void caijing_roll(){
        show("/caijing/roll");
    }

    @Test
    public void cls_telegraph(){
        show("/cls/telegraph");
    }
}