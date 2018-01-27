package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.service.SyncSendService;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.net.SocketException;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 * HTTP协议的同步发送服务
 */
@Slf4j
public class HttpSyncSendService implements SyncSendService {
    @Override
    public boolean send(InterfaceContext context) {
        ErrorContextFactory.instance().reset();
        InterfaceDefinition interfaceDefinition = context.getInterfaceDefinition();
        InterfaceData outerOutput = context.getOuterOutput();
        InterfaceData outerInput = context.getOuterInput();
        HttpRequest http = HttpRequest.post(interfaceDefinition.getGatewayUrl())
                .acceptGzipEncoding()
                .acceptCharset("UTF-8")
                .connectTimeout(interfaceDefinition.getHttpTimeoutSecond() * 1000)
                .readTimeout(interfaceDefinition.getHttpTimeoutSecond() * 1000)
                .useCaches(false)
                .contentType("application/json;text/plain", "UTF-8");
        try {
            if (log.isDebugEnabled()) {
                log.debug("调用远程HTTP'{}'开始, 发送数据'{}'", outerOutput.getCipherText(), interfaceDefinition.getGatewayUrl());
            }
            http.send(outerOutput.getCipherText());
            if (log.isDebugEnabled()) {
                log.debug("调用远程HTTP'{}'结束, HTTP CODE:{}", interfaceDefinition.getGatewayUrl(), http.code());
            }
        } catch (HttpRequest.HttpRequestException e) {
            Exception exception = e.getCause();
            if (exception instanceof ConnectException) {
                throw ErrorContextFactory.instance()
                        .activity("HTTP 同步发送")
                        .message("远程服务器'{}'不可连接", interfaceDefinition.getGatewayUrl())
                        .cause(exception)
                        .runtimeException();
            }
            if (exception instanceof SocketException && exception.getMessage().toLowerCase().contains("permission denied")) {
                throw ErrorContextFactory.instance()
                        .activity("HTTP 同步发送")
                        .message("当前服务器无权限使用网络")
                        .cause(exception)
                        .runtimeException();
            }
            log.error("同步发送数据发生错误", e);
            return false;
        }
        if (http.ok()) {
            if (log.isDebugEnabled()) {
                log.debug("调用远程HTTP通信成功, HTTP CODE:{}", http.code());
            }
            String response = http.body("UTF-8");
            outerInput.setCipherText(response);
            return true;
        } else if (http.notFound()) {
            throw ErrorContextFactory.instance()
                    .activity("HTTP 同步发送")
                    .message("同步发送数据发生错误，无法访问网关地址 '{}'", interfaceDefinition.getGatewayUrl())
                    .solution("请联系网关地址'{}'提供方!", interfaceDefinition.getGatewayUrl())
                    .runtimeException();
        } else {
            throw ErrorContextFactory.instance()
                    .activity("HTTP 同步发送")
                    .message("同步发送数据发生未知错误，网关地址 '{}'", interfaceDefinition.getGatewayUrl())
                    .solution("请联系网关地址'{}'提供方，查找服务器错误!", interfaceDefinition.getGatewayUrl())
                    .runtimeException();
        }
    }
}
