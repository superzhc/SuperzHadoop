package com.github.superzhc.fund.index;

/**
 * @author superz
 * @create 2022/4/21 14:54
 **/
public class IndexTool {
    public static void shengouCal(Double sg, Double sgf) {
        // 净金额
        Double jsg = sg / (1 + sgf * 0.01);
    }
}
