package com.juyou.common.sign;


import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * RSA公钥/私钥/签名工具包
 * 字符串格式的密钥在未特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 */
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 字符编码
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 常量0
     */
    private static final int ZERO = 0;

    /**
     * RSA最大加密明文最大大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文最大大小
     * 当密钥位数为1024时,解密密文最大是 128
     * 当密钥位数为2048时需要改为 256 不然会报错（Decryption error）
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /**
     * 默认key大小
     */
    private static final int DEFAULT_KEY_SIZE = 2048;

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return map
     * @throws Exception 异常
     */
    public static Map<String, Object> initKey() throws Exception {
        return initKey(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return map
     * @throws Exception 异常
     */
    public static Map<String, Object> initKey(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }


    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return byte
     * @throws Exception 异常
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return encrypt(data, KeyFactory.getInstance(KEY_ALGORITHM), keyFactory.generatePublic(x509KeySpec));
    }


    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return byte
     * @throws Exception 异常
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        return encrypt(data, keyFactory, privateK);
    }


    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return byte
     * @throws Exception 异常
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return decrypt(encryptedData, keyFactory, keyFactory.generatePrivate(pkcs8KeySpec));
    }


    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return byte
     * @throws Exception 异常
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        return decrypt(encryptedData, keyFactory, publicK);

    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return String
     * @throws Exception 异常
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return new String(Base64Utils.encode(signature.sign()), CHARSET_NAME);
    }


    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return boolean
     * @throws Exception 异常
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign.getBytes()));
    }


    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return String
     * @throws Exception 异常
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return new String(Base64Utils.encode(key.getEncoded()), CHARSET_NAME);
    }


    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return String
     * @throws Exception 异常
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return new String(Base64Utils.encode(key.getEncoded()), CHARSET_NAME);
    }

    /**
     * 解密公共方法
     */
    private static byte[] decrypt(byte[] data, KeyFactory keyFactory, Key key) throws Exception {

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return encryptAndDecrypt(data, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 加密公共方法
     */
    private static byte[] encrypt(byte[] data, KeyFactory keyFactory, Key key) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptAndDecrypt(data, cipher, MAX_ENCRYPT_BLOCK);
    }


    /**
     * 加密解密分段处理公共方法
     */
    private static byte[] encryptAndDecrypt(byte[] data, Cipher cipher, int maxSize) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = ZERO;
        byte[] cache;
        int i = ZERO;
        // 对数据分段加密
        while (inputLen - offSet > ZERO) {
            if (inputLen - offSet > maxSize) {
                cache = cipher.doFinal(data, offSet, maxSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, ZERO, cache.length);
            i++;
            offSet = i * maxSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


    /**
     * 分段加解密示例
     *
     * @throws Exception 异常
     */
    public static void encryptAndDecrypt() throws Exception {
        //生成公钥与私钥
        Map<String, Object> initKeyMap = RSAUtils.initKey();
        //公钥
        String publicKey = RSAUtils.getPublicKey(initKeyMap);
        //私钥
        String privateKey = RSAUtils.getPrivateKey(initKeyMap);
        System.out.println("公钥 长度: " + publicKey.length() + " 值: " + publicKey);
        System.out.println("私钥 长度: " + privateKey.length() + " 值: " + privateKey);

        String str = "--*-- 测试数据123456 --*--";

        byte[] bytes1 = RSAUtils.encryptByPublicKey(str.getBytes(), publicKey);
        byte[] bytes2 = RSAUtils.decryptByPrivateKey(bytes1, privateKey);

        System.out.println();
        System.out.println("****** 公钥加密 私钥解密 start ******");
        System.out.println("加密前长度 ：" + str.length());
        System.out.println("加密后 ：" + Base64.encodeBase64String(bytes1));
        System.out.println("解密后 ：" + new String(bytes2));
        System.out.println("解密后长度 ：" + new String(bytes2).length());
        System.out.println("****** 公钥加密 私钥解密 end ******");

        System.out.println();
        byte[] bytes3 = RSAUtils.encryptByPrivateKey(str.getBytes(), privateKey);
        byte[] bytes4 = RSAUtils.decryptByPublicKey(bytes3, publicKey);

        System.out.println("****** 私钥加密 公钥解密 start ******");
        System.out.println("加密前长度 ：" + str.length());
        System.out.println("加密后 ：" + Base64.encodeBase64String(bytes3));
        System.out.println("解密后 ：" + new String(bytes4));
        System.out.println("解密后长度 ：" + new String(bytes4).length());
        System.out.println("****** 私钥加密 公钥解密 end ******");
    }


    /**
     * RSA加签与验签示例
     *
     * @throws Exception 异常
     */
    private static void signAndVerify() throws Exception {
        //生成公钥与私钥
        Map<String, Object> initKeyMap = RSAUtils.initKey(1024);
        //公钥
        String publicKey = RSAUtils.getPublicKey(initKeyMap);
        //私钥
        String privateKey = RSAUtils.getPrivateKey(initKeyMap);
        System.out.println("公钥 长度: " + publicKey.length() + " 值: " + publicKey);
        System.out.println("私钥 长度: " + privateKey.length() + " 值: " + privateKey);

        String str = "--*-- 测试数据123456 --*--";

        //加签
        String sign = RSAUtils.sign(str.getBytes(), privateKey);
        //验签
        boolean verify = RSAUtils.verify(str.getBytes(), publicKey, sign);

        System.out.println();
        System.out.println("****** 加签 start ******");
        System.out.println("签名 sign: " + sign);
        System.out.println("验签结果: " + verify);
        System.out.println("****** 加签 end ******");
    }

    public static void main(String[] args) throws Exception {
        encryptAndDecrypt();

        signAndVerify();
    }
}
