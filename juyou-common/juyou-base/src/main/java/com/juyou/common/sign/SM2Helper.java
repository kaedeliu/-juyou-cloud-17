package com.juyou.common.sign;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.Security;

/**
 * @Description: 国密sm2非对称加解密算法帮助类
 * @Author: SGao 
 * @CreateDate: 2020/5/15
 */
public class SM2Helper {
//    public static final String PUBLIC_KEY_X = "2d3dcd87a32152585df20ae4a6446422a22561346e1a7b96aea9191116f0753a";
//    public static final String PUBLIC_KEY_Y = "60b6a34307f032b520e004b45a6cdb5b151438639b12a02ec7fd4f3738934ae0";
//    public static final String PRIVATE_KEY = "41dd9b10d3f94c406725580d8bd5fb4d1a7c23f5d67fd546f19e22c4e4c79be7";

    /*
     * 以下为SM2推荐曲线参数
     */
    public static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
    public final static BigInteger SM2_ECC_P = CURVE.getQ();
    public final static BigInteger SM2_ECC_A = CURVE.getA().toBigInteger();
    public final static BigInteger SM2_ECC_B = CURVE.getB().toBigInteger();
    public final static BigInteger SM2_ECC_N = CURVE.getOrder();
    public final static BigInteger SM2_ECC_H = CURVE.getCofactor();
    public final static BigInteger SM2_ECC_GX = new BigInteger(
            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    public final static BigInteger SM2_ECC_GY = new BigInteger(
            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
    public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
    public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G_POINT,
            SM2_ECC_N, SM2_ECC_H);

    public static final Charset charset = Charset.forName("utf-8");

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    /**
     * 构建公钥参数
     *
     * @param publicKeyX
     * @param publicKeyY
     * @return
     */
    public static ECPublicKeyParameters buildECPublicKeyParameters(String publicKeyX, String publicKeyY) {
        ECPoint pointQ = CURVE.createPoint(new BigInteger(publicKeyX, 16), new BigInteger(publicKeyY, 16));
        return new ECPublicKeyParameters(pointQ, DOMAIN_PARAMS);
    }


    /**
     * 构建私钥参数
     *
     * @param privateKey
     * @return
     */
    public static ECPrivateKeyParameters buildECPrivateKeyParameters(String privateKey) {
        BigInteger d = new BigInteger(privateKey, 16);
        return new ECPrivateKeyParameters(d, DOMAIN_PARAMS);
    }


    /**
     * 私钥签名
     *
     * @param input      待签名数据
     * @param privateKey 私钥数据
     * @param ID         用户标识
     * @return
     * @throws Exception
     */
    public static String sign(String input, String privateKey, String ID) throws Exception {
        byte[] inputByte = input.getBytes(charset);
        SM2Signer signer = new SM2Signer();
        CipherParameters param;
        ECPrivateKeyParameters ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
        if (ID != null) {
            byte[] IDByte = ID.getBytes(charset);
            param = new ParametersWithID(ecPrivateKeyParameters, IDByte);
        } else {
            param = ecPrivateKeyParameters;
        }
        signer.init(true, param);
        signer.update(inputByte, 0, inputByte.length);
        byte[] signature = signer.generateSignature();
        return Hex.toHexString(signature);
    }

//    public static String sign(String input, String ID) throws Exception {
//        return sign(input, PRIVATE_KEY, ID);
//    }


    /**
     * @Description
     * @Author kaedeliu
     * @param input 待验签数据
     * @param sign  签名
     * @param publicKeyX 公钥
     * @param publicKeyY 公钥
     * @param ID    用户标识
     * @Return boolean  验签结果
     */
    public static boolean verifySign(String input, String sign, String publicKeyX, String publicKeyY, String ID) throws Exception {
        byte[] inputByte = input.getBytes(charset);
        ECPublicKeyParameters ecPublicKeyParameters = buildECPublicKeyParameters(publicKeyX, publicKeyY);
        byte[] signByte = new BigInteger(sign, 16).toByteArray();

        SM2Signer signer = new SM2Signer();
        CipherParameters param;
        if (ID != null) {
            byte[] IDByte = ID.getBytes(charset);
            param = new ParametersWithID(ecPublicKeyParameters, IDByte);
        } else {
            param = ecPublicKeyParameters;
        }
        signer.init(false, param);
        signer.update(inputByte, 0, inputByte.length);
        return signer.verifySignature(signByte);
    }

//    public static boolean verifySign(String input, String sign, String ID) throws Exception {
//        return verifySign(input, sign, PUBLIC_KEY_X, PUBLIC_KEY_Y, ID);
//    }



    public static void main(String[] args) {
//        SM2KeyPair sm2KeyPair = SM2KeyHelper.generateKeyPair();
//        String publicKeyX = Hex.toHexString(sm2KeyPair.getPublicKeyX());
//        String publicKeyY = Hex.toHexString(sm2KeyPair.getPublicKeyY());
//        String privateKey = Hex.toHexString(sm2KeyPair.getPrivateKey());
//        System.out.println("publicKeyX:" + publicKeyX);
//        System.out.println("publicKeyY:" + publicKeyY);
//        System.out.println("privateKey:" + privateKey);
//
//        System.out.println("length:" + (privateKey+publicKeyX+publicKeyY).length());
//        String input = "{\"certificateType\":\"身份证\", \"certificateNumber\":\"500222199310146119\"}";
//
//        try {
//            //sign and verifySign without ID
//            String hexSign = SM2Helper.sign(input, null);
//            System.out.println("hexSign:"+hexSign);
//            boolean verifySignature = SM2Helper.verifySign(input, hexSign, null);
//            Assert.isTrue(verifySignature, "sign and verifySign without ID failed!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

}
