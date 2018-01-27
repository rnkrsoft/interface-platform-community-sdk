package com.rnkrsoft.platform;

import com.rnkrsoft.platform.service.SecurityService;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 * 安全服务
 */
@ToString
@Builder
@Data
public class InterfaceSecurity {
    /**
     * 验签算法
     */
    String verifyAlgorithm;
    /**
     * 签字算法
     */
    String signAlgorithm;
    /**
     * 解密算法
     */
    String decryptAlgorithm;
    /**
     * 加密算法
     */
    String encryptAlgorithm;
    /**
     * 密码
     */
    String password;
    /**
     * 秘钥向量
     */
    String keyVector;
    /**
     * 是否检验TOKEN
     */
    Boolean validateToken = false;
    /**
     * 验签先于解密
     */
    Boolean firstVerifySecondDecrypt = false;
    /**
     * 签字先于加密
     */
    Boolean firstSignSecondEncrypt = false;
    /**
     * 安全服务实现
     */
    SecurityService securityService;

    public InterfaceSecurity() {
    }

    public InterfaceSecurity(String verifyAlgorithm, String signAlgorithm, String decryptAlgorithm, String encryptAlgorithm, String password, String keyVector, Boolean validateToken, Boolean firstVerifySecondDecrypt, Boolean firstSignSecondEncrypt, SecurityService securityService) {
        this.verifyAlgorithm = verifyAlgorithm;
        this.signAlgorithm = signAlgorithm;
        this.decryptAlgorithm = decryptAlgorithm;
        this.encryptAlgorithm = encryptAlgorithm;
        this.password = password;
        this.keyVector = keyVector;
        this.validateToken = validateToken;
        this.firstVerifySecondDecrypt = firstVerifySecondDecrypt;
        this.firstSignSecondEncrypt = firstSignSecondEncrypt;
        this.securityService = securityService;
    }
}
