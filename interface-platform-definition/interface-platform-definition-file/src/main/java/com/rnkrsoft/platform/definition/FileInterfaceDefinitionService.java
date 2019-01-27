package com.rnkrsoft.platform.definition;

import com.rnkrsoft.platform.*;
import com.rnkrsoft.platform.definition.file.InterfaceDefinitionChannel;
import com.rnkrsoft.platform.definition.file.InterfaceDefinitionMetadata;
import com.rnkrsoft.platform.definition.file.InterfaceGlobalSetting;
import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.InterfaceDefinitionService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2019/1/26.
 */
@Slf4j
public class FileInterfaceDefinitionService implements InterfaceDefinitionService {
    String configPath = System.getProperty("user.dir");

    public static final String GLOBAL_SETTING = "global.json";

    final Map<String, InterfaceDefinitionMetadata> interfaceDefinitionMetadataMap = new HashMap<String, InterfaceDefinitionMetadata>();
    final Map<String, InterfaceDefinitionChannel> interfaceDefinitionChannelMap = new HashMap<String, InterfaceDefinitionChannel>();

    public FileInterfaceDefinitionService(String configPath) throws IOException {
        if (configPath != null && !configPath.isEmpty()) {
            this.configPath = configPath;
        }
        this.configPath = this.configPath + File.separator + "conf";
        init();
    }

    void init() throws IOException {
        InterfaceGlobalSetting globalSetting = loadGlobal();
        for (String channel : globalSetting.getChannels()) {
            InterfaceDefinitionChannel interfaceDefinitionChannel = loadChannel(channel);
            interfaceDefinitionChannelMap.put(channel, interfaceDefinitionChannel);
            for (InterfaceDefinitionMetadata interfaceDefinitionMetadata : interfaceDefinitionChannel.getInterfaces()) {
                String key = interfaceDefinitionMetadata.getChannel() + ":" + interfaceDefinitionMetadata.getTxNo() + ":" + interfaceDefinitionMetadata.getVersion();
                interfaceDefinitionMetadataMap.put(key, interfaceDefinitionMetadata);
            }
        }
    }

    InterfaceGlobalSetting loadGlobal() throws IOException {
        File globalFile = new File(configPath, GLOBAL_SETTING);
        if (!globalFile.exists()) {
            throw new FileNotFoundException(globalFile.getCanonicalPath() + " is not found!");
        }
        if (globalFile.isDirectory()) {
            throw new IllegalArgumentException(globalFile.getCanonicalPath() + " is Directory!");
        }
        return InterfaceGlobalSetting.load(globalFile);
    }

    InterfaceDefinitionChannel loadChannel(String channel) throws IOException {
        File channelFile = new File(configPath, channel + ".json");
        if (!channelFile.exists()) {
            throw new FileNotFoundException(channelFile.getCanonicalPath() + " is not found!");
        }
        if (channelFile.isDirectory()) {
            throw new IllegalArgumentException(channelFile.getCanonicalPath() + " is Directory!");
        }
        return InterfaceDefinitionChannel.load(channelFile);
    }

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
        String channel = interfaceDefinition.getChannel();
        String txNo = interfaceDefinition.getTxNo();
        String version = interfaceDefinition.getVersion();
        InterfaceDefinitionChannel interfaceDefinitionChannel = interfaceDefinitionChannelMap.get(channel);
        if (interfaceDefinitionChannel == null) {
            log.error("not found interface '{}:{}:{}', cause current version interface is not found!!", channel, txNo, version);
            context.getInnerResult().setEnum(InterfaceRspCode.INTERFACE_NOT_DEFINED);
            context.getOuterResult().setEnum(InterfaceRspCode.INTERFACE_NOT_DEFINED);
            return false;
        }
        if (!interfaceDefinitionChannel.isEnabled()) {
            context.getOuterResult().setEnum(InterfaceRspCode.CHANNEL_NOT_EXISTS);
            context.getInnerResult().setEnum(InterfaceRspCode.CHANNEL_NOT_EXISTS);
            return false;
        }
        String serialNo = channel + ":" + txNo + ":" + version;
        InterfaceDefinitionMetadata interfaceDefinitionMetadata = interfaceDefinitionMetadataMap.get(serialNo);
        interfaceSecurity.setDecryptAlgorithm(interfaceDefinitionMetadata.getDecryptAlgorithm());
        interfaceSecurity.setEncryptAlgorithm(interfaceDefinitionMetadata.getEncryptAlgorithm());
        interfaceSecurity.setVerifyAlgorithm(interfaceDefinitionMetadata.getVerifyAlgorithm());
        interfaceSecurity.setSignAlgorithm(interfaceDefinitionMetadata.getSignAlgorithm());
        //如果接口使用TOKEN作为加密解密密码，以及签字密码
        if (interfaceDefinitionMetadata.isUseTokenAsPassword()) {
            //不过本地配置为使用TOKEN令牌作为密码，则需要检查是否为null
            if (context.getToken() == null) {
                context.getInnerResult().setEnum(InterfaceRspCode.USE_TOKEN_AS_PASSWORD_BUT_TOKEN_IS_NULL);
                context.getOuterResult().setEnum(InterfaceRspCode.USE_TOKEN_AS_PASSWORD_BUT_TOKEN_IS_NULL);
                return false;
            } else {
                interfaceSecurity.setPassword(context.getToken());
            }
        } else {
            //如果接口使用固定密码，则使用接口定义的固定密码
            interfaceSecurity.setPassword(interfaceDefinitionChannel.getPassword());
        }
        interfaceSecurity.setKeyVector(interfaceDefinitionChannel.getKeyVector());
        //是否校验TOKEN
        interfaceSecurity.setValidateToken(interfaceDefinitionMetadata.isValidateToken());
        //签字先于加密
        interfaceSecurity.setFirstSignSecondEncrypt(interfaceDefinitionMetadata.isFirstSignSecondEncrypt());
        //验签先于解密
        interfaceSecurity.setFirstVerifySecondDecrypt(interfaceDefinitionMetadata.isFirstVerifySecondDecrypt());
        //通道名称
        interfaceDefinition.setChannelName(interfaceDefinitionChannel.getChannelName());
        //接口调用方向
        interfaceDefinition.setInterfaceDirection(InterfaceDirectionEnum.valueOfCode(interfaceDefinitionMetadata.getInterfaceDirection()));
        //同步通信时的网关地址
        interfaceDefinition.setGatewayUrl(interfaceDefinitionMetadata.getGatewayUrl());
        //HTTP同步通信时的超时时间
        interfaceDefinition.setHttpTimeoutSecond(interfaceDefinitionMetadata.getHttpTimeoutSecond());
        //是否记录通信报文
        interfaceDefinition.setWriteMessage(interfaceDefinitionMetadata.isWriteMessage());
        //信息写入模式
        interfaceDefinition.setWriteMode(WriteModeEnum.valueOfCode(interfaceDefinitionMetadata.getWriteMode()));
        //接口服务类名
        interfaceDefinition.setInterfaceClassName(interfaceDefinitionMetadata.getServiceClassName());
        //接口服务方法名
        interfaceDefinition.setInterfaceMethodName(interfaceDefinitionMetadata.getMethodName());
        //2. 获取交易定义信息
        Class serviceClass = null;
        Method interfaceMethod = interfaceDefinition.getInterfaceMethod();
        //双重校验接口方法对象的获取
        if (interfaceMethod == null) {
            synchronized (FileInterfaceDefinitionService.class){
                if (interfaceMethod == null){
                    try {
                        serviceClass = Class.forName(interfaceDefinition.getInterfaceClassName());
                        for (Method method : serviceClass.getMethods()) {
                            if (method.getName().equals(interfaceDefinition.getInterfaceMethodName())) {
                                interfaceMethod = method;
                                interfaceDefinition.setInterfaceMethod(interfaceMethod);
                                break;
                            }
                        }
                        if (interfaceMethod == null) {
                            log.error("交易码 '{}' 对应的服务类 '{}.{}' 不存在！", txNo, interfaceDefinition.getInterfaceClassName(), interfaceDefinition.getInterfaceMethodName());
                            outerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_METHOD_NOT_EXIST);
                            innerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_METHOD_NOT_EXIST);
                            return false;
                        }
                    } catch (ClassNotFoundException e) {
                        log.error("交易码 '{}' 对应的服务类 '{}.{}' 不存在！", txNo, interfaceDefinition.getInterfaceClassName(), interfaceDefinition.getInterfaceMethodName());
                        outerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_CLASS_NOT_FOUND);
                        innerResult.setEnum(InterfaceRspCode.INTERFACE_SERVICE_CLASS_NOT_FOUND);
                        return false;
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
}