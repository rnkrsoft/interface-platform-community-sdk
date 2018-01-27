/**
 * RNKRSOFT OPEN SOURCE SOFTWARE LICENSE TERMS ver.1
 * - 氡氪网络科技(重庆)有限公司 开源软件许可条款(版本1)
 * 氡氪网络科技(重庆)有限公司 以下简称Rnkrsoft。
 * 这些许可条款是 Rnkrsoft Corporation（或您所在地的其中一个关联公司）与您之间达成的协议。
 * 请阅读本条款。本条款适用于所有Rnkrsoft的开源软件项目，任何个人或企业禁止以下行为：
 * .禁止基于删除开源代码所附带的本协议内容、
 * .以非Rnkrsoft的名义发布Rnkrsoft开源代码或者基于Rnkrsoft开源源代码的二次开发代码到任何公共仓库,
 * 除非上述条款附带有其他条款。如果确实附带其他条款，则附加条款应适用。
 * <p>
 * 使用该软件，即表示您接受这些条款。如果您不接受这些条款，请不要使用该软件。
 * 如下所述，安装或使用该软件也表示您同意在验证、自动下载和安装某些更新期间传输某些标准计算机信息以便获取基于 Internet 的服务。
 * <p>
 * 如果您遵守这些许可条款，将拥有以下权利。
 * 1.阅读源代码和文档
 * 如果您是个人用户，则可以在任何个人设备上阅读、分析、研究Rnkrsoft开源源代码。
 * 如果您经营一家企业，则可以在无数量限制的设备上阅读Rnkrsoft开源源代码,禁止分析、研究Rnkrsoft开源源代码。
 * 2.编译源代码
 * 如果您是个人用户，可以对Rnkrsoft开源源代码以及修改后产生的源代码进行编译操作，编译产生的文件依然受本协议约束。
 * 如果您经营一家企业，不可以对Rnkrsoft开源源代码以及修改后产生的源代码进行编译操作。
 * 3.二次开发拓展功能
 * 如果您是个人用户，可以基于Rnkrsoft开源源代码进行二次开发，修改产生的元代码同样受本协议约束。
 * 如果您经营一家企业，不可以对Rnkrsoft开源源代码进行任何二次开发，但是可以通过联系Rnkrsoft进行商业授予权进行修改源代码。
 * 完整协议。本协议以及开源源代码附加协议，共同构成了Rnkrsoft开源软件的完整协议。
 * <p>
 * 4.免责声明
 * 该软件按“原样”授予许可。 使用本文档的风险由您自己承担。Rnkrsoft 不提供任何明示的担保、保证或条件。
 * 5.版权声明
 * 本协议所对应的软件为 Rnkrsoft 所拥有的自主知识版权，如果基于本软件进行二次开发，在不改变本软件的任何组成部分的情况下的而二次开发源代码所属版权为贵公司所有。
 */
package com.rnkrsoft.security;


import com.rnkrsoft.platform.protocol.utils.JavaEnvironmentDetector;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class AES {
    public static String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
    public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
    public static final String AES_CBC_PKCS7PADDING = "AES/CBC/PKCS7PADDING";
    public static String DEFAULT_IV = "1234567890654321";
    static int HASH_ITERATIONS = 1000;
    static byte[] SALT = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    static int KEY_LENGTH = 256;

    public static String encrypt(String key, String value) {
        return encrypt(key, DEFAULT_IV, value);
    }

    public static String encrypt(String key, String initVector, String plainText) {
        SecretKey sk = null;
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray(), SALT, HASH_ITERATIONS, KEY_LENGTH);
            sk = secretKeyFactory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密发生错误", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("加密发生错误", e);
        }
        // This is our secret key. We could just save this to a file instead of
        // regenerating it
        // each time it is needed. But that file cannot be on the device (too
        // insecure). It could
        // be secure if we kept it on a server accessible through https.
        byte[] skAsByteArray = sk.getEncoded();
        try {
            byte[] encodeBytes = Base64.encode(plainText.getBytes("UTF-8"), Base64.NO_WRAP);
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(skAsByteArray, "AES");
            Cipher cipher = Cipher.getInstance(JavaEnvironmentDetector.isAndroid() ? AES_CBC_PKCS7PADDING : AES_CBC_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(encodeBytes);
            byte[] bytes = Base64.encode(encrypted, Base64.NO_WRAP);
            return new String(bytes, "UTF-8");
        } catch (InvalidKeyException ex) {
            if (ex.getMessage().contains("Illegal key size")){
                System.err.println("please see http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html");
            }
            throw new RuntimeException("加密发生错误", ex);
        } catch (Exception ex) {
            throw new RuntimeException("加密发生错误", ex);
        }
    }


    public static String decrypt(String key, String value) {
        return decrypt(key, DEFAULT_IV, value);
    }

    public static String decrypt(String key, String initVector, String cipherText) {
        SecretKey sk = null;
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray(), SALT, HASH_ITERATIONS, KEY_LENGTH);
            sk = secretKeyFactory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("解密发生错误", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("解密发生错误", e);
        }
        byte[] skAsByteArray = sk.getEncoded();
        try {
            byte[] data = Base64.decode(cipherText, Base64.NO_WRAP);//使用不换行模式
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(skAsByteArray, "AES");
            Cipher cipher = Cipher.getInstance(JavaEnvironmentDetector.isAndroid() ? AES_CBC_PKCS7PADDING : AES_CBC_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] original = cipher.doFinal(data);
            byte[] output = Base64.decode(original, Base64.NO_WRAP);//使用不换行模式
            return new String(output, "UTF-8");
        } catch (InvalidKeyException ex) {
            if (ex.getMessage().contains("Illegal key size")){
                System.err.println("please see http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html");
            }
            throw new RuntimeException("解密发生错误", ex);
        } catch (Exception ex) {
            throw new RuntimeException("解密发生错误", ex);
        }
    }
}