package com.github.superzhc.common.utils;

/**
 * 2020年06月08日 superz add
 */
public class ByteUtils
{
    public static String byte2Hexs(byte... bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
