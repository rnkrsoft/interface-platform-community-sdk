package com.rnkrsoft.platform.proxy;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.annotation.InterfaceSender;
import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.jdbc.service.MySQLInterfaceDefinitionService;
import com.rnkrsoft.platform.jdbc.service.MySQLMessageService;
import com.rnkrsoft.platform.service.InterfaceDefinitionService;
import com.rnkrsoft.platform.service.InterfaceEngine;
import com.rnkrsoft.platform.service.MessageService;
import com.rnkrsoft.platform.service.SyncSendService;
import com.rnkrsoft.platform.service.impl.BootstrapInterfaceEngine;
import com.rnkrsoft.platform.service.impl.HttpSyncSendService;
import com.rnkrsoft.platform.service.impl.SpringBeanExecuteService;
import com.rnkrsoft.platform.spring.SpringContextHelper;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 */
@Slf4j
public class InterfaceSenderProxy implements InvocationHandler {


    public InterfaceSenderProxy() {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InterfaceContext ctx = InterfaceContext.builder()
                .requestNo(UUID.randomUUID().toString())
                .responseNo(UUID.randomUUID().toString())
                .sessionId(UUID.randomUUID().toString())
                .build();
        InterfaceData innerOutput = ctx.getInnerOutput();
        InterfaceData innerInput = ctx.getInnerInput();
        InterfaceDefinition interfaceDefinition = ctx.getInterfaceDefinition();
        interfaceDefinition.setInterfaceDirection(InterfaceDirectionEnum.OUTER);
        interfaceDefinition.setInterfaceMethod(method);
        innerOutput.setObject(args[0]);
        innerInput.setObjectClass(method.getReturnType());
        Class serviceClass = method.getDeclaringClass();
        InterfaceSender interfaceSender = (InterfaceSender) serviceClass.getAnnotation(InterfaceSender.class);
        ApidocService apidocService = (ApidocService) serviceClass.getAnnotation(ApidocService.class);
        ApidocInterface apidocInterface = method.getAnnotation(ApidocInterface.class);
        if (interfaceSender == null) {
            throw ErrorContextFactory.instance().message("类'{}'上未标注@InterfaceSender", serviceClass.getName()).runtimeException();
        }
        if (apidocService == null) {
            throw ErrorContextFactory.instance().message("类'{}'上未标注@ApidocService", serviceClass.getName()).runtimeException();
        }
        if (apidocInterface == null) {
            throw ErrorContextFactory.instance().message("类'{}'的方法'{}'上未标注@ApidocInterface", serviceClass.getName(), method.getName()).runtimeException();
        }
        String interfaceDefinitionServiceClassName = interfaceSender.interfaceDefinitionServiceClassName();
        String interfaceEngineClassName = interfaceSender.interfaceEngineClassName();
        String messageServiceClassName = interfaceSender.messageServiceClassName();
        String syncSendServiceClassName = interfaceSender.syncSendServiceClassName();
        if (StringUtils.isBlank(interfaceDefinitionServiceClassName)) {
            interfaceDefinitionServiceClassName = StringUtils.firstCharToLower(MySQLInterfaceDefinitionService.class.getSimpleName());
        }
        if (StringUtils.isBlank(interfaceEngineClassName)) {
            interfaceEngineClassName = StringUtils.firstCharToLower(BootstrapInterfaceEngine.class.getSimpleName());
        }
        if (StringUtils.isBlank(messageServiceClassName)) {
            messageServiceClassName = StringUtils.firstCharToLower(MySQLMessageService.class.getSimpleName());
        }
        if (StringUtils.isBlank(syncSendServiceClassName)) {
            syncSendServiceClassName = StringUtils.firstCharToLower(HttpSyncSendService.class.getSimpleName());
        }
        ErrorContextFactory.instance().activity("创建接口发送器");
        if (!SpringContextHelper.containsBean(interfaceDefinitionServiceClassName)){
            throw ErrorContextFactory.instance()
                    .message("接口定义服务'{}'未配置", InterfaceDefinitionService.class)
                    .solution("在Spring 配置文件或者注解配置对象中进行配置")
                    .runtimeException();
        }
        if (!SpringContextHelper.containsBean(interfaceEngineClassName)){
            throw ErrorContextFactory.instance()
                    .message("接口引擎'{}'未配置", InterfaceEngine.class)
                    .solution("在Spring 配置文件或者注解配置对象中进行配置")
                    .runtimeException();
        }
        if (!SpringContextHelper.containsBean(messageServiceClassName)){
            throw ErrorContextFactory.instance()
                    .message("接口信息服务'{}'未配置", MessageService.class)
                    .solution("在Spring 配置文件或者注解配置对象中进行配置")
                    .runtimeException();
        }
        if (!SpringContextHelper.containsBean(syncSendServiceClassName)){
            throw ErrorContextFactory.instance()
                    .message("同步发送服务'{}'未配置", SyncSendService.class)
                    .solution("在Spring 配置文件或者注解配置对象中进行配置")
                    .runtimeException();
        }
        InterfaceDefinitionService interfaceDefinitionService = (InterfaceDefinitionService) SpringContextHelper.getBean(interfaceDefinitionServiceClassName);
        BootstrapInterfaceEngine interfaceEngine = (BootstrapInterfaceEngine) SpringContextHelper.getBean(interfaceEngineClassName);
        MessageService messageService = (MessageService) SpringContextHelper.getBean(messageServiceClassName);
        SyncSendService syncSendService = (SyncSendService) SpringContextHelper.getBean(syncSendServiceClassName);
        String version = null;
        if (!apidocInterface.version().isEmpty()) {
            version = apidocInterface.version();
        }
        if (!apidocService.version().isEmpty() && apidocInterface.version().isEmpty()) {
            version = apidocService.version();
        }
        if (!apidocService.version().isEmpty() && !apidocInterface.version().isEmpty()) {
            version = "1";
        }
        interfaceDefinition.setChannel(interfaceSender.channel());
        interfaceDefinition.setTxNo(apidocInterface.name());
        interfaceDefinition.setVersion(version);
        interfaceDefinition.setAsync(interfaceSender.async());
        interfaceDefinition.setExecuteService(new SpringBeanExecuteService());
        interfaceDefinition.setSyncSendService(syncSendService);
        interfaceDefinition.setOrderMessageService(messageService);
        if (!interfaceDefinitionService.setting(ctx)) {
            if (log.isDebugEnabled()) {
                log.debug("setting interface happens error, json :{}", ctx);
            }
            throw new RuntimeException("setting interface happens error");
        }
        if (interfaceEngine.execute(ctx)) {
            if (log.isDebugEnabled()) {
                log.debug("call gateway , json :{}", ctx);
            }
            return innerInput.getObject();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("call gateway happens error, json :{}", ctx);
            }
            throw new RuntimeException(ctx.getInnerResult().getCode() + ":" + ctx.getInnerResult().getDesc());
        }
    }
}
