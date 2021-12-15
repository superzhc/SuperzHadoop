package com.github.superzhc.data.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * @author superz
 * @create 2021/12/14 19:47
 */
public class PinYinUtils {
    private static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

    static {
        // 控制大小写
        // UPPERCASE：大写  (ZHONG)
        // LOWERCASE：小写  (zhong)
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // WITHOUT_TONE：无音标  (zhong)
        // WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)
        // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // WITH_V：用v表示ü  (nv)
        // WITH_U_AND_COLON：用"u:"表示ü  (nu:)
        // WITH_U_UNICODE：直接用ü (nü)
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public static String pinyin(String str) {
        if (null == str || str.trim().length() == 0) {
            return str;
        }

        str = str.trim();

        try {
            StringBuilder result = new StringBuilder();
            for (int i = 0, len = str.length(); i < len; i++) {
                // 判断是否为汉字
                if (String.valueOf(str.charAt(i)).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] ss = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), defaultFormat);
                    if (null != ss && ss.length > 0) {
                        result.append(ss[0]);
                    }
                } else {
                    result.append(str.charAt(i));
                }
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
