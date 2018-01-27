package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.protocol.ApiRequest;
import com.rnkrsoft.platform.protocol.ApiResponse;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/5/24.
 */
@Slf4j
public class InterfaceServiceImpl implements InterfaceService, ApplicationContextAware, InitializingBean {
    @Setter
    ApplicationContext applicationContext;

    @Autowired(required = false)
    TokenService tokenService;

    @Autowired
    BootstrapInterfaceEngine interfaceEngine;

    @Autowired
    MessageService messageService;

    @Autowired(required = false)
    SyncSendService syncSendService;

    @Autowired
    InterfaceDefinitionService interfaceDefinitionService;

    @Setter
    String[] allowProvinces;

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public ApiResponse invoke(ApiRequest request, String clientIp, int clientPort) {
        ErrorContextFactory.instance().reset();
        ApiResponse response = new ApiResponse();
        String txNo = request.getTxNo();
        if (txNo == null || txNo.isEmpty()) {
            log.error("交易码 '{}' 为空", txNo);
            response.setCode(InterfaceRspCode.TX_NO_IS_NULL);
            return response;
        }
        InterfaceContext ctx = InterfaceContext.builder()
                .sessionId(request.getSessionId())
                .deviceManufacturer(request.getDeviceManufacturer())
                .deviceModel(request.getDeviceModel())
                .deviceType(request.getDeviceType())
                .macAddress(request.getMacAddress())
                .osVersion(request.getOsVersion())
                .appVersion(request.getAppVersion())
                .uic(request.getUic())
                .uid(request.getUid())
                .token(request.getToken())
                .lat(request.getLat())
                .lng(request.getLng())
                .timestamp(request.getTimestamp())
                .clientIp(clientIp)
                .clientPort(clientPort)
                .requestNo(UUID.randomUUID().toString())
                .responseNo(UUID.randomUUID().toString())
                .build();
        InterfaceData outerInput = ctx.getOuterInput();
        //接口定义信息
        InterfaceDefinition interfaceDefinition = ctx.getInterfaceDefinition();
        outerInput.setCipherText(request.getData());
        outerInput.setObject(request);
        outerInput.setSign(request.getSign());
        interfaceDefinition.setChannel(request.getChannel());
        interfaceDefinition.setTxNo(request.getTxNo());
        interfaceDefinition.setVersion(request.getVersion());
        interfaceDefinition.setExecuteService(new SpringBeanExecuteService());
        interfaceDefinition.setSyncSendService(syncSendService);
        interfaceDefinition.setOrderMessageService(messageService);
        interfaceDefinition.setTokenService(tokenService);
        if (interfaceDefinitionService.setting(ctx) && interfaceEngine.execute(ctx)){
            response.setData(ctx.getOuterOutput().getCipherText());
            response.setSign(ctx.getOuterOutput().getSign());
            response.setCode(InterfaceRspCode.SUCCESS);
            return response;
        }else{
            response.setCode(ctx.getOuterResult().getCode());
            response.setDesc(ctx.getOuterResult().getDesc());
            return response;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("  _____           _                    __                           _____    _           _      __                                 _____   _____    _  __\n" +
                " |_   _|         | |                  / _|                         |  __ \\  | |         | |    / _|                               / ____| |  __ \\  | |/ /\n" +
                "   | |    _ __   | |_    ___   _ __  | |_    __ _    ___    ___    | |__) | | |   __ _  | |_  | |_    ___    _ __   _ __ ___     | (___   | |  | | | ' / \n" +
                "   | |   | '_ \\  | __|  / _ \\ | '__| |  _|  / _` |  / __|  / _ \\   |  ___/  | |  / _` | | __| |  _|  / _ \\  | '__| | '_ ` _ \\     \\___ \\  | |  | | |  <  \n" +
                "  _| |_  | | | | | |_  |  __/ | |    | |   | (_| | | (__  |  __/   | |      | | | (_| | | |_  | |   | (_) | | |    | | | | | |    ____) | | |__| | | . \\ \n" +
                " |_____| |_| |_|  \\__|  \\___| |_|    |_|    \\__,_|  \\___|  \\___|   |_|      |_|  \\__,_|  \\__| |_|    \\___/  |_|    |_| |_| |_|   |_____/  |_____/  |_|\\_\\\n" +
                "                                                                                                                                                         \n" +
                "                                                                                                                                                         ");
    }
}
