package com.rnkrsoft.platform.annotation;

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
    String interfaceDefinitionServiceClassName() default "";
    /**
     * 消息服务类
     * @return 消息服务类
     */
    String  messageServiceClassName() default  "";
    /**
     * 接口引擎类
     * @return 引擎类
     */
    String interfaceEngineClassName() default "";
    /**
     * 同步发送服务
     * @return 同步发送服务
     */
    String syncSendServiceClassName() default "";
}
