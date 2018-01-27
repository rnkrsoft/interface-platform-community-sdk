package com.rnkrsoft.platform.jdbc.service;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.InterfaceResult;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.jdbc.dao.InterfaceRequestDAO;
import com.rnkrsoft.platform.jdbc.dao.InterfaceResponseDAO;
import com.rnkrsoft.platform.jdbc.entity.InterfaceRequestEntity;
import com.rnkrsoft.platform.jdbc.entity.InterfaceResponseEntity;
import com.rnkrsoft.platform.service.AsyncMessageQueueMessageService;
import com.rnkrsoft.platform.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 */
@Slf4j
public class MySQLMessageService extends AsyncMessageQueueMessageService implements MessageService {
    @Autowired
    InterfaceRequestDAO interfaceRequestDAO;
    @Autowired
    InterfaceResponseDAO interfaceResponseDAO;


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String createRequestSync(InterfaceContext context,
                                    String requestNo,
                                    String responseNo,
                                    InterfaceData innerData,
                                    InterfaceData outerData,
                                    OrderStatusEnum orderStatus,
                                    Date createDate,
                                    Date lastUpdateDate) {
        if (log.isDebugEnabled()) {
            log.debug("begin to sync write request '{}' order sessionId '{}'", requestNo, context.getSessionId());
        }
        InterfaceDefinition interfaceDefinition = context.getInterfaceDefinition();
        InterfaceRequestEntity requestEntity = InterfaceRequestEntity.builder()
                .requestNo(requestNo)
                .responseNo(responseNo)
                .sessionId(context.getSessionId())
                .channel(interfaceDefinition.getChannel())
                .txNo(interfaceDefinition.getTxNo())
                .version(interfaceDefinition.getVersion())
                .deviceManufacturer(context.getDeviceManufacturer())
                .deviceModel(context.getDeviceModel())
                .deviceType(context.getDeviceType())
                .osVersion(context.getOsVersion())
                .appVersion(context.getAppVersion())
                .macAddress(context.getMacAddress())
                .uic(context.getUic())
                .uid(context.getUid())
                .lat(new BigDecimal(Double.toString(context.getLat() == null ? 0 : context.getLat())))
                .lng(new BigDecimal(Double.toString(context.getLng() == null ? 0 : context.getLng())))
                .clientIp(context.getClientIp())
                .clientPort(context.getClientPort())
                .innerMessage(innerData.getPlainText())
                .outerMessage(outerData.getCipherText())
                .orderStatus(orderStatus.getCode())
                .build();
        requestEntity.setCreateDate(createDate);
        requestEntity.setLastUpdateDate(lastUpdateDate);
        interfaceRequestDAO.insertSelective(requestEntity);
        return requestEntity.getRequestNo();
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String createResponseSync(InterfaceContext context,
                                     String responseNo,
                                     String requestNo,
                                     RspTypeEnum rspType,
                                     InterfaceData innerData,
                                     InterfaceData outerData,
                                     InterfaceResult innerResult,
                                     InterfaceResult outerResult,
                                     Date createDate,
                                     Date lastUpdateDate
    ) {
        if (log.isDebugEnabled()) {
            log.debug("begin to sync write response '{}' order requestNo '{}'", responseNo, requestNo);
        }
        InterfaceResponseEntity interfaceResponseEntity = InterfaceResponseEntity.builder()
                .responseNo(responseNo)
                .requestNo(requestNo)
                .rspType(rspType.getCode())
                .innerMessage(context.getInterfaceDefinition().isWriteMessage() ? innerData.getPlainText() : "已关闭记录通信信息")
                .outerMessage(context.getInterfaceDefinition().isWriteMessage() ? outerData.getCipherText() : "已关闭记录通信信息")
                .innerRspCode(innerResult.getCode())
                .innerRspDesc(innerResult.getDesc())
                .outerRspCode(outerResult.getCode())
                .outerRspDesc(outerResult.getDesc())
                .build();
        interfaceResponseEntity.setCreateDate(createDate);
        interfaceResponseEntity.setLastUpdateDate(lastUpdateDate);
        interfaceResponseDAO.insertSelective(interfaceResponseEntity);
        return interfaceResponseEntity.getResponseNo();
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String createResponseSync(InterfaceContext context,
                                     String responseNo,
                                     String requestNo,
                                     RspTypeEnum rspType,
                                     InterfaceData innerData,
                                     InterfaceData outerData,
                                     InterfaceResult innerResult,
                                     InterfaceResult outerResult,
                                     String causeStackTrace,
                                     String causeMessage,
                                     Date createDate,
                                     Date lastUpdateDate) {
        if (log.isDebugEnabled()) {
            log.debug("begin to sync write response '{}' order requestNo '{}'", responseNo, requestNo);
        }
        InterfaceResponseEntity interfaceResponseEntity = InterfaceResponseEntity.builder()
                .responseNo(responseNo)
                .requestNo(requestNo)
                .rspType(rspType.getCode())
                .innerMessage(context.getInterfaceDefinition().isWriteMessage() ? innerData.getPlainText() : "已关闭记录通信信息")
                .outerMessage(context.getInterfaceDefinition().isWriteMessage() ? outerData.getCipherText() : "已关闭记录通信信息")
                .innerRspCode(innerResult.getCode())
                .innerRspDesc(innerResult.getDesc())
                .outerRspCode(outerResult.getCode())
                .outerRspDesc(outerResult.getDesc())
                .causeStackTrace(causeStackTrace)
                .causeMessage(causeMessage)
                .build();
        interfaceResponseEntity.setCreateDate(createDate);
        interfaceResponseEntity.setLastUpdateDate(lastUpdateDate);
        interfaceResponseDAO.insertSelective(interfaceResponseEntity);
        return interfaceResponseEntity.getResponseNo();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public long updateRequestStatus(InterfaceContext interfaceContext, WriteModeEnum writeModeEnum, String requestNo, OrderStatusEnum expectOrderStatus, OrderStatusEnum orderStatus) {
        if (log.isDebugEnabled()) {
            log.debug("begin to sync update requestNo '{}'", requestNo);
        }
        return interfaceRequestDAO.updateStatus(requestNo, expectOrderStatus.getCode(), orderStatus.getCode());
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public long updateRequestStatus(InterfaceContext interfaceContext, WriteModeEnum writeModeEnum, String requestNo, OrderStatusEnum expectOrderStatus, OrderStatusEnum orderStatus, String responseNo) {
        if (log.isDebugEnabled()) {
            log.debug("begin to sync update requestNo '{}'", requestNo);
        }
        return interfaceRequestDAO.updateStatusAndRspNo(requestNo, expectOrderStatus.getCode(), orderStatus.getCode(), responseNo);
    }

    @Override
    public String queryRequestBySessionId(InterfaceContext interfaceContext, String sessionId, OrderStatusEnum expectOrderStatus) {
        return null;
    }
}
