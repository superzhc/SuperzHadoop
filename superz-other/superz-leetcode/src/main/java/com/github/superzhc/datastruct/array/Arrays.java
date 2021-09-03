package com.github.superzhc.datastruct.array;

/**
 * 2020年07月02日 superz add
 */
public class Arrays
{
    public static String human(int[] arr) {
        if (null == arr)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0, len = arr.length; i < len; i++) {
            if (i != 0)
                sb.append(",");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void print(int[] arr) {
        System.out.println(human(arr));
    }
}
