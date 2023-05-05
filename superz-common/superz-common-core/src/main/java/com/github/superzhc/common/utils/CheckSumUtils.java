package com.github.superzhc.common.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;

/**
 * @author superz
 * @create 2023/5/5 10:26
 **/
public class CheckSumUtils {
    public enum Type {

        MD5("MD5"), SHA_256("SHA-256"), SHA_512("SHA-512"), SHA_1("SHA1");

        private String algorithm;

        private Type(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return this.algorithm;
        }
    }

    private CheckSumUtils() {
    }

    public static String checksum(File file, Type type) {
        return checksum(file, type, true);
    }

    public static String checksum(File file, Type type, boolean toLowerCase) {
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在");
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(type.getAlgorithm());
            messageDigest.update(Files.readAllBytes(file.toPath()));
            byte[] digestBytes = messageDigest.digest();

//            StringBuffer sb = new StringBuffer();
//            for (byte b : digestBytes) {
//                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
//            }
//            String result= sb.toString();

            // 注意：DatatypeConverter.printHexBinary(digestBytes)返回的字符大写，默认为小写
            String result = DatatypeConverter.printHexBinary(digestBytes);
            if (toLowerCase) {
                result = result.toLowerCase();
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String path = "D:\\徐工\\mnist_keras_predict.py";
        File file = new File(path);

        String outTemplate = "使用算法[%s]的 CHECKSUM 值为：%s\n";
        System.out.printf(outTemplate, Type.MD5.getAlgorithm(), checksum(file, Type.MD5));
        System.out.printf(outTemplate, Type.SHA_1.getAlgorithm(), checksum(file, Type.SHA_1));
        System.out.printf(outTemplate, Type.SHA_256.getAlgorithm(), checksum(file, Type.SHA_256));
        System.out.printf(outTemplate, Type.SHA_512.getAlgorithm(), checksum(file, Type.SHA_512));
    }
}
