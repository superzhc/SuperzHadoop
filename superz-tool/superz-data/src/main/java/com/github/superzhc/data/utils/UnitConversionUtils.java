package com.github.superzhc.data.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单位转换
 *
 * @author superz
 * @create 2022/2/17 23:45
 */
public class UnitConversionUtils {
    public static Double number(String str) {
        if (null == str || str.trim().length() == 0) {
            return 0.0;
        }

        str = str.trim().replace(",", "");

        Double number = 0.0;
        String unit = "";
        if (str.endsWith("万")) {
            int len = str.length();
            unit = str.substring(len - 1);
        }

        String regex = "\\d+([.]\\d*)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            number = Double.parseDouble(matcher.group());
        }

        switch (unit) {
            case "万":
                return number * 10000;
            default:
                return number;
        }
    }

}
