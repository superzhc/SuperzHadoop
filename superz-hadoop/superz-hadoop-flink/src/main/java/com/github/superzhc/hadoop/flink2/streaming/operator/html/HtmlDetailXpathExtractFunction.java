package com.github.superzhc.hadoop.flink2.streaming.operator.html;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.seimicrawler.xpath.JXDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author superz
 * @create 2022/9/29 17:50
 **/
public class HtmlDetailXpathExtractFunction extends RichMapFunction<String, Map<String, String>> {
    private static final Logger log = LoggerFactory.getLogger(HtmlDetailXpathExtractFunction.class);


    @Override
    public Map<String, String> map(String html) throws Exception {
        JXDocument doc = JXDocument.create(html);
        return null;
    }
}
