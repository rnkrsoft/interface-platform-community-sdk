package com.rnkrsoft.platform.service.impl;

import com.google.gson.JsonSyntaxException;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.protocol.*;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.ExecuteService;
import com.rnkrsoft.platform.spring.SpringContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 */
@Slf4j
public class SpringBeanExecuteService implements ExecuteService, InitializingBean {
    final static Map<String, Method> METHODS_CACHE = new HashMap();

    @Override
    public boolean execute(InterfaceContext context) throws Throwable {
        ErrorContextFactory.instance().reset();
        InterfaceDefinition definition = context.getInterfaceDefinition();
        String name = definition.getInterfaceClassName() + ":" + definition.getInterfaceMethodName();
        Method interfaceMethod = METHODS_CACHE.get(name);
        Class serviceClass = null;
        if (interfaceMethod == null) {
            synchronized (this) {
                try {
                    serviceClass = Class.forName(definition.getInterfaceClassName());
                    for (Method method : serviceClass.getMethods()) {
                        if (method.getName().equals(definition.getInterfaceMethodName())) {
                            interfaceMethod = method;
                            break;
                        }
                    }
                    if (interfaceMethod == null) {
                        log.error("交易码 '{}' 对应的服务类 '{}.{}' 不存在！", definition.getTxNo(), definition.getInterfaceClassName(), definition.getInterfaceMethodName());
                        context.getInnerResult().setEnum(InterfaceRspCode.INTERFACE_SERVICE_METHOD_NOT_EXIST);
                        context.getOuterResult().setEnum(InterfaceRspCode.INTERFACE_SERVICE_METHOD_NOT_EXIST);
                        return false;
                    }
                } catch (ClassNotFoundException e) {
                    log.error("交易码 '{}' 对应的服务类 '{}.{}' 不存在！", definition.getTxNo(), definition.getInterfaceClassName(), definition.getInterfaceMethodName());
                    context.getInnerResult().setEnum(InterfaceRspCode.INTERFACE_SERVICE_CLASS_NOT_FOUND);
                    context.getOuterResult().setEnum(InterfaceRspCode.INTERFACE_SERVICE_CLASS_NOT_FOUND);
                    return false;
                }
                METHODS_CACHE.put(name, interfaceMethod);
            }
        } else {
            serviceClass = interfaceMethod.getDeclaringClass();
        }
        Object request = null;
        try {
            request = context.getInnerInput().getObject();
        } catch (JsonSyntaxException e) {
            log.error("业务请求报文'{}'无法转换成'{}'!", context.getInnerInput().getPlainText(), context.getInnerInput().getObjectClass());
            context.getInnerResult().setEnum(InterfaceRspCode.REQUEST_DATA_ILLEGAL);
            context.getOuterResult().setEnum(InterfaceRspCode.REQUEST_DATA_ILLEGAL);
            return false;
        }
        //设置会话号到请求中，保证分布式环境下的日志跟踪
        if (request instanceof SessionIdWritable || request instanceof SessionIdAble) {
            SessionIdWritable sessionIdWritable = (SessionIdWritable) request;
            sessionIdWritable.setSessionId(context.getSessionId());
        }
        //将用户号和用户姓名设置到对象中
        if (request instanceof UserInfoWritable || request instanceof UserInfoAble) {
            UserInfoWritable userInfoWritable = (UserInfoWritable) request;
            userInfoWritable.setUserId(context.getUid());
            userInfoWritable.setUserName(context.getUname());
        }
        //设置经纬度到请求中
        if (request instanceof GisPosWritable || request instanceof GisPosAble) {
            GisPosWritable gisPosWritable = (GisPosWritable) request;
            gisPosWritable.setLng(Double.toString(context.getLng() == null ? 0 : context.getLng()));
            gisPosWritable.setLat(Double.toString(context.getLat() == null ? 0 : context.getLat()));
        }
        //设置网络地址信息
        if (request instanceof ClientNetworkWritable || request instanceof ClientNetworkAble) {
            ClientNetworkWritable clientNetworkWritable = (ClientNetworkWritable) request;
            clientNetworkWritable.setClientIp(context.getClientIp());
            clientNetworkWritable.setClientPort(context.getClientPort());
        }
        //设置通道信息
        if (request instanceof ChannelInfoWritable || request instanceof ChannelInfoAble) {
            ChannelInfoWritable channelInfoWritable = (ChannelInfoWritable) request;
            channelInfoWritable.setChannelNo(context.getInterfaceDefinition().getChannel());
            channelInfoWritable.setChannelName(context.getInterfaceDefinition().getChannelName());
        }
        //设置设备信息
        if (request instanceof DeviceInfoWritable || request instanceof DeviceInfoAble) {
            DeviceInfoWritable deviceInfoWritable = (DeviceInfoWritable) request;
            deviceInfoWritable.setDeviceManufacturer(context.getDeviceManufacturer());
            deviceInfoWritable.setDeviceModel(context.getDeviceModel());
            deviceInfoWritable.setDeviceType(context.getDeviceType());
            deviceInfoWritable.setOsVersion(context.getOsVersion());
            deviceInfoWritable.setAppVersion(context.getAppVersion());
        }
        try {
            Object instance = SpringContextHelper.getBean(serviceClass);
            Object result = interfaceMethod.invoke(instance, request);
            context.getInnerOutput().setObject(result);
            return true;
        } catch (IllegalAccessException e) {
            log.error("execute service happens error!", e);
            throw e;
        } catch (InvocationTargetException e1) {
            Throwable e = e1.getTargetException();
            log.error("execute service happens error!", e);
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Interface platform use Spring Execute Service...");
    }
}
