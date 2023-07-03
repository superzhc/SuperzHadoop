package com.github.superzhc.hadoop.nifi.processors.nlp;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.nifi.components.AllowableValue;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;

import java.util.List;

/**
 * HanLP 分词
 * <p>
 * HanLP 1.X文档地址：https://github.com/hankcs/HanLP/tree/1.x
 *
 * @author superz
 * @create 2023/7/3 20:34
 */
public class HanLPSegmentProcessor extends AbstractProcessor {

    public static final AllowableValue STANDARD_TOKENIZER = new AllowableValue("StandardTokenizer", "标准分词", "");
    public static final AllowableValue NLP_TOKENIZER = new AllowableValue("NLPTokenizer", "NLP分词", "");
    public static final AllowableValue INDEX_TOKENIZER = new AllowableValue("IndexTokenizer", "索引分词", "");
    // public static final AllowableValue _TOKENIZER=new AllowableValue("","N-最短路径分词","");
    // public static final AllowableValue _TOKENIZER=new AllowableValue("CRFLexicalAnalyzer","CRF分词","");
    public static final AllowableValue SPEED_TOKENIZER = new AllowableValue("SpeedTokenizer", "极速词典分词", "");

    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        String text = "";

        // 标准分词
        List<Term> termList = StandardTokenizer.segment(text);
    }
}
