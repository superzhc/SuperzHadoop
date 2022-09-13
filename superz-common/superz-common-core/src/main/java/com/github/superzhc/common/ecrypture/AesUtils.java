package com.github.superzhc.common.ecrypture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author superz
 * @create 2022/9/13 17:24
 **/
public class AesUtils {
    private static final Logger log = LoggerFactory.getLogger(AesUtils.class);

    /**
     * key 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 固定值
     */
    private static final String SECRET_RANDOM = "SHA1PRNG";

    /**
     * 编码方式
     */
    public static final String ENCODING_TYPE = "UTF-8";

    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";


    /**
     * 加密
     *
     * @param content
     * @param password
     * @return
     */
    public static String encrypt(String content, String password) {

        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            byte[] byteContent = content.getBytes(ENCODING_TYPE);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            // 通过Base64转码返回
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error("aesencrypt000 error ", e);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result, "utf-8");
        } catch (Exception e) {
            log.error("aesdecrypt000 error ", e);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance(SECRET_RANDOM);
            secureRandom.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, secureRandom);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error("aesgetSecretKey000 error ", e);
        }

        return null;
    }

    public static void main(String[] args) {
        String content = "郑超";
        String password = "superz";

        String content2 = encrypt(content, password);
        System.out.println(content2);
        System.out.println(decrypt(content2, password));
    }

}
