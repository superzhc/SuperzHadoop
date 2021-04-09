package com.github.superzhc.collection;

import java.util.List;

/**
 * @author superz
 * @create 2021/4/8 15:49
 */
public class ListUtils {
    public static String list2String(List<?> lst) {
        return list2String(lst, " ");
    }

    public static String list2String(List<?> lst, String separator) {
        StringBuilder sb = new StringBuilder();
        if (null == lst || lst.size() == 0) {
            return null;
        }

        int size = lst.size();
        for (int i = 0, len = size - 1; i < len; i++) {
            sb.append(lst.get(i)).append(separator);
        }
        sb.append(lst.get(size - 1));
        return sb.toString();
    }
}
