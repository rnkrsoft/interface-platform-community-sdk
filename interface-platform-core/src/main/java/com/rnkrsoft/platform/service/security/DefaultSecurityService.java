package com.rnkrsoft.platform.service.security;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceSecurity;
import com.rnkrsoft.platform.service.SecurityService;
import com.rnkrsoft.security.AES;
import com.rnkrsoft.security.SHA;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 * 默认安全服务
 */
@Slf4j
public class DefaultSecurityService implements SecurityService {
    @Override
    public boolean sign(InterfaceContext context, InterfaceData data) {
        InterfaceSecurity interfaceSecurity = context.getInterfaceSecurity();
        if (interfaceSecurity.getSignAlgorithm() == null || interfaceSecurity.getSignAlgorithm().isEmpty()){
            if (log.isDebugEnabled()){
                log.debug("未配置签字算法，不进行签字处理");
            }
            return true;
        }
        if (interfaceSecurity.getPassword() == null){
            throw ErrorContextFactory.instance().activity("签字处理")
                    .message("签字'{}'使用的密码为空", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的密码是否正确")
                    .runtimeException();
        }
        if ("SHA512".equalsIgnoreCase(interfaceSecurity.getSignAlgorithm())) {
            if (interfaceSecurity.getFirstSignSecondEncrypt()) {
                String sign = SHA.SHA512(data.getPlainText() + context.getSessionId() + context.getTimestamp() + interfaceSecurity.getPassword());
                if (sign == null) {
                    return false;
                }
                data.setSign(sign);
                if (log.isDebugEnabled()){
                    log.debug("data: '{}', sign: '{}'", data.getPlainText(), sign);
                }
            } else {
                String sign = SHA.SHA512(data.getCipherText() + context.getSessionId() + context.getTimestamp() + interfaceSecurity.getPassword());
                if (sign == null) {
                    return false;
                }
                if (log.isDebugEnabled()){
                    log.debug("data: '{}', sign: '{}'", data.getPlainText(), sign);
                }
                data.setSign(sign);
            }
            return true;
        }else{
           throw ErrorContextFactory.instance().activity("签字处理")
                   .message("不支持的签字算法'{}'", interfaceSecurity.getSignAlgorithm())
                   .solution("检查配置的算法是否正确")
                   .runtimeException();
        }

    }

    @Override
    public boolean verify(InterfaceContext context, InterfaceData data) {
        InterfaceSecurity interfaceSecurity = context.getInterfaceSecurity();
        if (interfaceSecurity.getVerifyAlgorithm() == null || interfaceSecurity.getVerifyAlgorithm().isEmpty()){
            if (log.isDebugEnabled()){
                log.debug("未配置验签算法，不进行验签处理");
            }
            return true;
        }
        if (interfaceSecurity.getPassword() == null){
            throw ErrorContextFactory.instance().activity("验签处理")
                    .message("验签'{}'使用的密码为空", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的密码是否正确")
                    .runtimeException();
        }
        if ("SHA512".equalsIgnoreCase(interfaceSecurity.getVerifyAlgorithm())) {
            if (interfaceSecurity.getFirstVerifySecondDecrypt()) {
                String sign = SHA.SHA512(data.getCipherText() + context.getSessionId() + context.getTimestamp() + interfaceSecurity.getPassword());
                if (sign == null) {
                    return false;
                }
                if (log.isDebugEnabled()){
                    log.debug("data: '{}', sign: '{}' calc sign: '{}'", data.getCipherText(), data.getSign(), sign);
                }
                return sign.equals(data.getSign());
            } else {
                String sign = SHA.SHA512(data.getPlainText() + context.getSessionId() + context.getTimestamp() + interfaceSecurity.getPassword());
                if (sign == null) {
                    return false;
                }
                if (log.isDebugEnabled()){
                    log.debug("data: '{}', sign: '{}' calc sign: '{}'", data.getCipherText(), data.getSign(), sign);
                }
                return sign.equals(data.getSign());
            }
        }else{
            throw ErrorContextFactory.instance().activity("验签处理")
                    .message("不支持的验签算法'{}'", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的算法是否正确")
                    .runtimeException();
        }
    }

    @Override
    public boolean encrypt(InterfaceContext context, InterfaceData data) {
        InterfaceSecurity interfaceSecurity = context.getInterfaceSecurity();
        if (interfaceSecurity.getEncryptAlgorithm() == null || interfaceSecurity.getEncryptAlgorithm().isEmpty()){
            if (log.isDebugEnabled()){
                log.debug("未配置加密算法，不进行加密处理");
            }
            data.setCipherText(data.getPlainText());
            return true;
        }
        if (interfaceSecurity.getPassword() == null){
            throw ErrorContextFactory.instance().activity("加密处理")
                    .message("加密'{}'使用的密码为空", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的密码是否正确")
                    .runtimeException();
        }
        if ("AES".equalsIgnoreCase(interfaceSecurity.getEncryptAlgorithm())) {
            try {
                ErrorContextFactory.instance().activity("AES encrypt message");
                String cipherText = AES.encrypt(interfaceSecurity.getPassword(), interfaceSecurity.getKeyVector(), data.getPlainText());
                if (cipherText == null) {
                    return false;
                }
                data.setCipherText(cipherText);
            } catch (Exception e) {
                log.error("encrypt happens error", e);
                return false;
            }
            return true;
        } else {
            throw ErrorContextFactory.instance().activity("加密处理")
                    .message("不支持的加密算法'{}'", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的算法是否正确")
                    .runtimeException();
        }
    }

    @Override
    public boolean decrypt(InterfaceContext context, InterfaceData data) {
        InterfaceSecurity interfaceSecurity = context.getInterfaceSecurity();
        if (interfaceSecurity.getDecryptAlgorithm() == null || interfaceSecurity.getDecryptAlgorithm().isEmpty()){
            if (log.isDebugEnabled()){
                log.debug("未配置解密算法，不进行解密处理");
            }
            data.setPlainText(data.getCipherText());
            return true;
        }
        if (interfaceSecurity.getPassword() == null){
            throw ErrorContextFactory.instance().activity("解密处理")
                    .message("解密'{}'使用的密码为空", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的密码是否正确")
                    .runtimeException();
        }
        if ("AES".equalsIgnoreCase(interfaceSecurity.getDecryptAlgorithm())) {
            try {
                ErrorContextFactory.instance().activity("AES decrypt message");
                String plainText = AES.decrypt(interfaceSecurity.getPassword(), interfaceSecurity.getKeyVector(), data.getCipherText());
                if (plainText == null) {
                    return false;
                }
                data.setPlainText(plainText);
            } catch (Exception e) {
                log.error("decrypt happens error", e);
                return false;
            }
            return true;
        } else {
            throw ErrorContextFactory.instance().activity("解密处理")
                    .message("不支持的解密算法'{}'", interfaceSecurity.getSignAlgorithm())
                    .solution("检查配置的算法是否正确")
                    .runtimeException();
        }
    }
}
