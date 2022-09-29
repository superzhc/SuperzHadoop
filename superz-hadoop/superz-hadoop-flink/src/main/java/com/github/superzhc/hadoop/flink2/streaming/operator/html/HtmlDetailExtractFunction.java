package com.github.superzhc.hadoop.flink2.streaming.operator.html;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/29 17:40
 **/
@Deprecated
public class HtmlDetailExtractFunction<Out> extends RichMapFunction<String,Out> {
    private static final Logger log= LoggerFactory.getLogger(HtmlDetailExtractFunction.class);

    private Map<String,String> keyAndCssSelector;

    // todo 输出结果是否需要有序

    public HtmlDetailExtractFunction(Map<String,String> keyAndCssSelector){
        this.keyAndCssSelector=keyAndCssSelector;
    }

    @Override
    public Out map(String html) throws Exception {
        Document doc= Jsoup.parse(html);

        Map<String,String> result;
        result=new HashMap<>();

        for(Map.Entry<String,String> item: keyAndCssSelector.entrySet()){
            // fixme 假如需要获取属性值
            String value=doc.selectFirst(item.getValue()).text();
        }

        return null;
    }
}
