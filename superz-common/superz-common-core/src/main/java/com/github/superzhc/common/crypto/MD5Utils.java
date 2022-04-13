package com.github.superzhc.common.crypto;

import com.github.superzhc.common.utils.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author superz
 * @create 2022/4/12 9:04
 **/
public class MD5Utils {
    public static String encrypt32(String str) {
        String hexStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
            hexStr = ByteUtils.byte2Hexs(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexStr;
    }

    public static String encrypt16(String str) {
        String hexStr = encrypt32(str);
        return null == hexStr || hexStr.length() != 32 ? null : hexStr.substring(8, 24);
    }

    public static void main(String[] args) {
        String str = "101_3_2.0+/api/v3/account/api/login/qrcode+\"ALDQ6KDrjxSPTg_x0e60DkwloFJLXTeUycc=|1646035719\"+3_2.0ae3TnRUTEvOOUCNMTQnTSHUZo02p-HNMZBO8YD_qo6Ppb7tqXRFZQi90-LS9-hp1DufI-we8gGHPgJO1xuPZ0GxCTJHR7820XM20cLRGDJXfgGCBxupMuD_Ie8FL7AtqM6O1VDQyQ6nxrRPCHukMoCXBEgOsiRP0XL2ZUBXmDDV9qhnyTXFMnXcTF_ntRueThHof9gg9F9XVBwxOB8VLQQ9_oBpmrCommMLmb9OytUgxDCO1vhOYr6pfWqOPv_tB3Ux1rwSYAwVYrGeVpXwKagwOZwwLkcXGM6OfCC3C6JSXc7OYih3LebHY-up_oXcGZCgK27O8S_YBc4NYNcxq9UXfihgBtwFB4JOYoicLhJO1VCHC5qfzhGNYQw3mxBtfNBY0QeNGZhxL3US0NwH1qUC_pg28gvLBMwNBo9NfiqYVcG3KGGx0bHN0rMSVk7LOT9OBQU3MrQLpS0V9dhw9ggU1Cw3sjwXyuqeLQ8eC";
        System.out.println(encrypt32(str));
        System.out.println(encrypt16(str));
    }
}
