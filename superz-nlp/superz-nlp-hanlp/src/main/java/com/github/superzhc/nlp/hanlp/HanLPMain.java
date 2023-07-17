package com.github.superzhc.nlp.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

public class HanLPMain {
    public static void main(String[] args) {
        String str = "泰坦军团 P28H2V IPS 28英寸显示器（3840*2160） 2199元";

        List<Term> tems = HanLP.newSegment()
                .enablePartOfSpeechTagging(true)
                .enableOrganizationRecognize(true)
                .seg(str);
        for (Term term : tems) {
            System.out.println(term.word);
        }
    }
}
