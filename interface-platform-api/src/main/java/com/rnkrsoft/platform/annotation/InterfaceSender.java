package com.rnkrsoft.platform.annotation;

import com.rnkrsoft.platform.service.InterfaceDefinitionService;
import com.rnkrsoft.platform.service.InterfaceEngine;
import com.rnkrsoft.platform.service.MessageService;
import com.rnkrsoft.platform.service.SyncSendService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 * 通道定义
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InterfaceSender {
    /**
     * 通道号
     * @return 通道号
     */
    String channel();

    /**
     * 是否为异步接口
     * @return 是否为异步接口
     */
    boolean async() default false;
    /**
     * 接口定义服务类
     * @return 接口定义服务类
     */
    Class<InterfaceDefinitionService> interfaceDefinitionServiceClass() default InterfaceDefinitionService.class;
    /**
     * 消息服务类
     * @return 消息服务类
     */
    Class<MessageService>  messageServiceClass() default  MessageService.class;
    /**
     * 接口引擎类
     * @return 引擎类
     */
    Class<InterfaceEngine> interfaceEngineClass() default InterfaceEngine.class;
    /**
     * 同步发送服务
     * @return 同步发送服务
     */
    Class<SyncSendService> syncSendServiceClass() default SyncSendService.class;
}
