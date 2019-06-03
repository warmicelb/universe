package com.ice.universe.common.test;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密工具
 * @author: ice
 * @create: 2019/3/4
 **/
public class RSATool {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException {
        //KeyPairGenerator用来构建公钥私钥对，通过指定特定的加密算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //初始化生成器，秘钥的大小为1024位
        keyPairGenerator.initialize(1024,new SecureRandom());
        //生成一个秘钥对，保存在KeyPair类的对象中
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //获取私钥对象
        PrivateKey privateKey = keyPair.getPrivate();
        //获取公钥对象
        PublicKey publicKey = keyPair.getPublic();
        //转化为字符串标识法（base64编码）
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        System.out.println(privateKeyStr);
        System.out.println(publicKeyStr);

        String origin = "附近的设计费拉十几个了几代人撒个健康的撒；放个假生气了卡丁车";
        //公钥加密操
        //1.生成publickey
        byte[] keyBytes = Base64.decodeBase64(publicKeyStr);
        PublicKey publicKey1 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr)));
        //2.开始加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey1);
        String encodedStr = Base64.encodeBase64String(cipher.doFinal(origin.getBytes()));
        System.out.println("加密后内容："+encodedStr);
        //私钥解密
        //1.生成privateKey
        PrivateKey privateKey1 = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)));
        //2.开始解密
        Cipher cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE,privateKey1);
        String decodeStr = Base64.encodeBase64String(cipher1.doFinal(encodedStr.getBytes()));
        System.out.println("解密后内容："+decodeStr);
    }
}
