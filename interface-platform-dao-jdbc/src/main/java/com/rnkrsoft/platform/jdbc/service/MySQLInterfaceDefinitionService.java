package com.rnkrsoft.platform.jdbc.service;

import com.rnkrsoft.platform.*;
import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.jdbc.dao.InterfaceDefinitionDAO;
import com.rnkrsoft.platform.jdbc.entity.InterfaceDefinitionEntity;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.InterfaceDefinitionService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/25.
 */
@Slf4j
public class MySQLInterfaceDefinitionService implements InterfaceDefinitionService, InitializingBean {
    /**
     * 是否使用本地缓存
     */
    @Setter
    boolean useLocalCache = true;

    private final static Map<String, Method> INTERFACE_METHOD_CACHE = new HashMap();
    private final static Map<String, InterfaceDefinitionEntity> INTERFACE_DEFINITION_CACHE = new HashMap();
    @Autowired
    InterfaceDefinitionDAO interfaceDefinitionDAO;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public boolean setting(InterfaceContext context) {
        InterfaceDefinition interfaceDefinition = context.getInterfaceDefinition();
        InterfaceSecurity interfaceSecurity = context.getInterfaceSecurity();
        InterfaceData outerInput = context.getOuterInput();
        InterfaceData innerInput = context.getInnerInput();
        InterfaceData innerOutput = context.getInnerOutput();
        InterfaceData outerOutput = context.getOuterOutput();
        InterfaceResult outerResult = context.getOuterResult();
        InterfaceResult innerResult = context.getInnerResult();
        String serialNo = interfaceDefinition.getChannel() + ":" + interfaceDefinition.getTxNo() + ":" + interfaceDefinition.getVersion();

        InterfaceDefinitionEntity interfaceDefinitionEntity = null;
        //启用本地缓存时从本地缓存中尝试获取
        if (useLocalCache) {
            if (log.isDebugEnabled()){
                log.debug("在本地缓存中获取接口定义信息");
            }
            interfaceDefinitionEntity = INTERFACE_DEFINITION_CACHE.get(serialNo);
        }
        if (interfaceDefinitionEntity == null) {
            synchronized (InterfaceDefinitionService.class) {
                interfaceDefinitionEntity = INTERFACE_DEFINITION_CACHE.get(serialNo);
                if (interfaceDefinitionEntity == null) {//如果双重校验缓存不存在定义
                    if (log.isDebugEnabled()){
                        log.debug("在本地缓存中获取接口定义信息，未命中，从数据库获取");
                    }
                    interfaceDefinitionEntity = interfaceDefinitionDAO.selectByPrimaryKey(serialNo);
                    if (interfaceDefinitionEntity == null) {//如果数据库不存在接口定义
                        List<InterfaceDefinitionEntity> interfaceDefinitionEntities = interfaceDefinitionDAO.selectAnd(InterfaceDefinitionEntity.builder().channel(interfaceDefinition.getChannel()).txNo(interfaceDefinition.getTxNo()).build());
                        if (!interfaceDefinitionEntities.isEmpty()) {
                            for (InterfaceDefinitionEntity entity : interfaceDefinitionEntities) {
                                log.error("found interface '{}:{}:{}'", entity.getChannel(), entity.getTxNo(), entity.getVersion());
                            }
                            log.error("not found interface '{}:{}:{}', cause current version interface is not found!!", interfaceDefinition.getChannel(), interfaceDefinition.getTxNo(), interfaceDefinition.getVersion());
                            context.getInnerResult().setEnum(InterfaceRspCode.INTERFACE_EXISTS_OTHER_VERSION);
                            context.getOuterResult().setEnum(InterfaceRspCode.INTERFACE_EXISTS_OTHER_VERSION);
                            return false;
                        } else {
                            int count = interfaceDefinitionDAO.countAnd(InterfaceDefinitionEntity.builder().channel(interfaceDefinition.getChannel()).build());
                            if (count == 0) {
                                log.error("not found interface '{}:{}:{}', cause channel is not exists!", interfaceDefinition.getChannel(), interfaceDefinition.getTxNo(), interfaceDefinition.getVersion());
                                context.getInnerResult().setEnum(InterfaceRspCode.CHANNEL_NOT_EXISTS);
                                context.getOuterResult().setEnum(InterfaceRspCode.CHANNEL_NOT_EXISTS);
                            } else {
                                log.error("not found interface '{}:{}:{}', cause interface is not defined! ", interfaceDefinition.getChannel(), interfaceDefinition.getTxNo(), interfaceDefinition.getVersion());
                                context.getInnerResult().setEnum(InterfaceRspCode.INTERFACE_NOT_DEFINED);
                                context.getOuterResult().setEnum(InterfaceRspCode.INTERFACE_NOT_DEFINED);
                            }
                            return false;
                        }
                    }
                }
            }
        }else{
            if (log.isDebugEnabled()){
                log.debug("在本地缓存中获取接口定义信息,已命中");
            }
        }
        interfaceSecurity.setDecryptAlgorithm(interfaceDefinitionEntity.getDecryptAlgorithm());
        interfaceSecurity.setEncryptAlgorithm(interfaceDefinitionEntity.getEncryptAlgorithm());
        interfaceSecurity.setVerifyAlgorithm(interfaceDefinitionEntity.getVerifyAlgorithm());
        interfaceSecurity.setSignAlgorithm(interfaceDefinitionEntity.getSignAlgorithm());
        //如果接口使用TOKEN作为加密解密密码，以及签字密码
        if (interfaceDefinitionEntity.getUseTokenAsPassword()) {
            //不过本地配置为使用TOKEN令牌作为密码，则需要检查是否为null
            if (context.getToken() == null) {
                context.getInnerResult().setEnum(InterfaceRspCode.USE_TOKEN_AS_PASSWORD_BUT_TOKEN_IS_NULL);
                context.getOuterResult().setEnum(InterfaceRspCode.USE_TOKEN_AS_PASSWORD_BUT_TOKEN_IS_NULL);
                return false;
            }else {
                interfaceSecurity.setPassword(context.getToken());
            }
        }else{
            //如果接口使用固定密码，则使用接口定义的固定密码
            interfaceSecurity.setPassword(interfaceDefinitionEntity.getPassword());
        }
        interfaceSecurity.setKeyVector(interfaceDefinitionEntity.getKeyVector());
        //是否校验TOKEN
        interfaceSecurity.setValidateToken(interfaceDefinitionEntity.getValidateToken());
        //签字先于加密
        interfaceSecurity.setFirstSignSecondEncrypt(interfaceDefinitionEntity.getFirstSignSecondEncrypt());
        //验签先于解密
        interfaceSecurity.setFirstVerifySecondDecrypt(interfaceDefinitionEntity.getFirstVerifySecondDecrypt());
        //TODO 通道名称
        interfaceDefinition.setChannelName(interfaceDefinitionEntity.getChannel());
        //接口调用方向
        interfaceDefinition.setInterfaceDirection(InterfaceDirectionEnum.valueOfCode(interfaceDefinitionEntity.getInterfaceDirection()));
        //同步通信时的网关地址
        interfaceDefinition.setGatewayUrl(interfaceDefinitionEntity.getGatewayUrl());
        //HTTP同步通信时的超时时间
        interfaceDefinition.setHttpTimeoutSecond(interfaceDefinitionEntity.getHttpTimeoutSecond());
        //是否记录通信报文
        interfaceDefinition.setWriteMessage(interfaceDefinitionEntity.getWriteMessage());
        //信息写入模式
        interfaceDefinition.setWriteMode(WriteModeEnum.valueOfCode(interfaceDefinitionEntity.getWriteMode()));
        //接口服务类名
        interfaceDefinition.setInterfaceClassName(interfaceDefinitionEntity.getServiceClassName());
        //接口服务方法名
        interfaceDefinition.setInterfaceMethodName(interfaceDefinitionEntity.getMethodName());
        //2. 获取交易定义信息
        Class serviceClass = null;
        Method interfaceMethod = interfaceDefinition.getInterfaceMethod();
        if (interfaceMethod == null) {
            //启用本地缓存时从本地缓存中尝试获取
            if (useLocalCache) {
                interfaceMethod = INTERFACE_METHOD_CACHE.get(interfaceDefinition.getTxNo() + ":" + interfaceDefinition.getVersion());
            }
            //双重校验接口方法对象的获取
            if (interfaceMethod == null) {
                synchronized (InterfaceDefinitionService.class) {
                    interfaceMethod = INTERFACE_METHOD_CACHE.get(interfaceDefinition.getTxNo() + ":" + interfaceDefinition.getVersion());
                    if (interfaceMethod == null) {
                        try {
                            serviceClass = Class.forName(interfaceDefinition.getInterfaceClassName());
                            for (Method method : serviceClass.getMethods()) {
                                if (method.getName().equals(interfaceDefinition.getInterfaceMethodName())) {
                                    interfaceMethod = method;
                                    break;
                                }
                            }
                            if (interfaceMethod == null) {
                                log.error("交易码 '{}' 对应的服务类 '{}.{}' 不存在！", interfaceDefinition.getTxNo(), interfaceDefinition.getInterfaceClassName(), interfaceDefinition.getInterfaceMethodName());
                                outerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_METHOD_NOT_EXIST);
                                innerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_METHOD_NOT_EXIST);
                                return false;
                            }
                        } catch (ClassNotFoundException e) {
                            log.error("交易码 '{}' 对应的服务类 '{}.{}' 不存在！", interfaceDefinition.getTxNo(), interfaceDefinition.getInterfaceClassName(), interfaceDefinition.getInterfaceMethodName());
                            outerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_CLASS_NOT_FOUND);
                            innerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_CLASS_NOT_FOUND);
                            return false;
                        }
                    }
                }
            }
        }
        if (interfaceDefinition.getInterfaceDirection() == InterfaceDirectionEnum.INNER) {
            if (innerInput.getObjectClass() == null) {
                innerInput.setObjectClass(interfaceMethod.getParameterTypes()[0]);
            }
            if (innerOutput.getObjectClass() == null) {
                innerOutput.setObjectClass(interfaceMethod.getReturnType());
            }
        } else if (interfaceDefinition.getInterfaceDirection() == InterfaceDirectionEnum.OUTER) {
            if (innerOutput.getObjectClass() == null) {
                innerOutput.setObjectClass(interfaceMethod.getParameterTypes()[0]);
            }
            if (innerInput.getObjectClass() == null) {
                innerInput.setObjectClass(interfaceMethod.getReturnType());
            }
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (useLocalCache){
            log.info("启用接口定义信息服务本地缓存");
        }else{
            log.info("禁用接口定义信息服务本地缓存");
        }
    }
}
