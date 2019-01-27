package com.rnkrsoft.platform;

import com.rnkrsoft.platform.domains.Request;
import com.rnkrsoft.platform.domains.Response;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.*;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;


/**
 * Created by rnkrsoft.com on 2019/1/26.
 */
@Slf4j
public class InterfaceServiceImpl implements InterfaceService {

    InterfaceDefinitionService interfaceDefinitionService;
    ExecuteService executeService;
    SyncSendService syncSendService;
    MessageService messageService;
    TokenService tokenService;
    InterfaceEngine interfaceEngine;
    FlowLimitingService flowLimitingService;

    @Override
    public Response invoke(Request request) {
        Response response = new Response();
        if (StringUtils.isBlank(request.getTimestamp())) {
            response.setCode(InterfaceRspCode.PARAM_IS_NULL);
            return response;
        }
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
                .clientIp(request.getClientIp())
                .clientPort(request.getClientPort())
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
        interfaceDefinition.setExecuteService(executeService);
        interfaceDefinition.setSyncSendService(syncSendService);
        interfaceDefinition.setOrderMessageService(messageService);
        interfaceDefinition.setTokenService(tokenService);
        if (!interfaceDefinitionService.setting(ctx) && interfaceEngine.execute(ctx)){
            response.setData(ctx.getOuterOutput().getCipherText());
            response.setSign(ctx.getOuterOutput().getSign());
            response.setCode(InterfaceRspCode.SUCCESS);
            return response;
        }else {
            response.setCode(ctx.getOuterResult().getCode());
            response.setDesc(ctx.getOuterResult().getDesc());
            return response;
        }
    }

    public void setInterfaceDefinitionService(InterfaceDefinitionService interfaceDefinitionService) {
        this.interfaceDefinitionService = interfaceDefinitionService;
    }

    public void setExecuteService(ExecuteService executeService) {
        this.executeService = executeService;
    }

    public void setSyncSendService(SyncSendService syncSendService) {
        this.syncSendService = syncSendService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setInterfaceEngine(InterfaceEngine interfaceEngine) {
        this.interfaceEngine = interfaceEngine;
    }

    public void setFlowLimitingService(FlowLimitingService flowLimitingService) {
        this.flowLimitingService = flowLimitingService;
    }

    @Override
    public FlowLimitingService getFlowLimitingService() {
        return flowLimitingService;
    }
}
