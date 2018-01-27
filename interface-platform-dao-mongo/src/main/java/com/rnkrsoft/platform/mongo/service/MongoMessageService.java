package com.rnkrsoft.platform.mongo.service;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.InterfaceResult;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.mongo.dao.MongoInterfaceRequestDAO;
import com.rnkrsoft.platform.mongo.dao.MongoInterfaceResponseDAO;
import com.rnkrsoft.platform.mongo.entity.MongoInterfaceRequestEntity;
import com.rnkrsoft.platform.mongo.entity.MongoInterfaceResponseEntity;
import com.rnkrsoft.platform.service.AsyncMessageQueueMessageService;
import com.rnkrsoft.platform.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/6/26.
 */
@Slf4j
public class MongoMessageService extends AsyncMessageQueueMessageService implements MessageService {
    @Autowired
    MongoInterfaceRequestDAO interfaceRequestDAO;
    @Autowired
    MongoInterfaceResponseDAO interfaceResponseDAO;

    @Override
    public String createRequestSync(InterfaceContext context,
                                    String requestNo,
                                    String response,
                                    InterfaceData innerData,
                                    InterfaceData outerData,
                                    OrderStatusEnum orderStatus,
                                    Date createDate,
                                    Date lastUpdateDate) {
        InterfaceDefinition interfaceDefinition = context.getInterfaceDefinition();
        MongoInterfaceRequestEntity entity = MongoInterfaceRequestEntity.builder()
                .requestNo(requestNo)
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
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
        interfaceRequestDAO.insert(entity);
        return requestNo;
    }


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
                                     Date lastUpdateDate) {
        return createResponseSync(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, null, null,createDate, lastUpdateDate);
    }


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
        MongoInterfaceResponseEntity entity = MongoInterfaceResponseEntity.builder()
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
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
        interfaceResponseDAO.insertSelective(entity);
        return responseNo;
    }


    @Override
    public long updateRequestStatus(InterfaceContext interfaceContext, WriteModeEnum writeModeEnum, String requestNo, OrderStatusEnum expectOrderStatus, OrderStatusEnum orderStatus) {
        return updateRequestStatus(interfaceContext, writeModeEnum, requestNo, expectOrderStatus, orderStatus, null);
    }


    @Override
    public long updateRequestStatus(InterfaceContext interfaceContext, WriteModeEnum writeModeEnum, String requestNo, OrderStatusEnum expectOrderStatus, OrderStatusEnum orderStatus, String responseNo) {
        MongoInterfaceRequestEntity condition = MongoInterfaceRequestEntity.builder()
                .requestNo(requestNo)
                .orderStatus(expectOrderStatus.getCode())
                .build();
        MongoInterfaceRequestEntity entity = MongoInterfaceRequestEntity.builder()
                .orderStatus(orderStatus.getCode())
                .responseNo(responseNo)
                .lastUpdateDate(new Date())
                .build();
        return interfaceRequestDAO.updateSelective(condition, entity);
    }


    @Override
    public String queryRequestBySessionId(InterfaceContext interfaceContext, String sessionId, OrderStatusEnum expectOrderStatus) {
        List<MongoInterfaceRequestEntity> list = interfaceRequestDAO.select(MongoInterfaceRequestEntity.builder().sessionId(sessionId).orderStatus(expectOrderStatus.getCode()).build());
        if (list.isEmpty()) {
            log.error("sessionId '{}' 未查找到请求记录", sessionId);
            throw ErrorContextFactory.instance()
                    .activity("MongoDB获取数据")
                    .message("通过会话号'{}'未查找到请求记录", sessionId)
                    .solution("检查数据库是否存在脏数据或者确认外部接口错误")
                    .runtimeException();
        } else if (list.size() == 1) {
            return list.get(0).getRequestNo();
        } else {
            log.error("sessionId '{}' 存在多个记录", sessionId);
            throw ErrorContextFactory.instance()
                    .activity("MongoDB获取数据")
                    .message("sessionId '{}' 存在多个记录, {}", sessionId, list)
                    .solution("检查数据库是否存在脏数据")
                    .runtimeException();
        }
    }
}
