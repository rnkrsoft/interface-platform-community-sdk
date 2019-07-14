package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceResult;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.service.MessageService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by woate on 2019/7/12.
 */
@Slf4j
public class Slf4jMessageServiceImpl implements MessageService {
    @Override
    public void setAsyncWrite(boolean asyncWrite) {

    }

    @Override
    public void init() {

    }

    @Override
    public String createRequest(InterfaceContext context, WriteModeEnum writeOrderMode, String requestNo, String responseNo, InterfaceData innerData, InterfaceData outerData, OrderStatusEnum orderStatus, Date createDate, Date lastUpdateDate) {
        log.info("request no:{}, inner message:{}, outer message:{}, order status:{}", requestNo, innerData.getPlainText(), outerData.getPlainText(), orderStatus.getCode());
        return "";
    }

    @Override
    public String createResponse(InterfaceContext context, WriteModeEnum writeOrderMode, String responseNo, String requestNo, RspTypeEnum rspType, InterfaceData innerData, InterfaceData outerData, InterfaceResult innerResult, InterfaceResult outerResult, Date createDate, Date lastUpdateDate) {
        log.info("response no:{}, inner message:{}, outer message:{}, inner status:{}, outer status:{}", responseNo, innerData.getPlainText(), outerData.getPlainText(), innerResult, outerResult);
        return "";
    }

    @Override
    public String createResponse(InterfaceContext context, WriteModeEnum writeOrderMode, String responseNo, String requestNo, RspTypeEnum rspType, InterfaceData innerData, InterfaceData outerData, InterfaceResult innerResult, InterfaceResult outerResult, String causeStackTrace, String causeMessage, Date createDate, Date lastUpdateDate) {
        log.error("response no:{}, inner message:{}, outer message:{}, inner status:{}, outer status:{}, causeStackTrace:{}, causeMessage:{}", responseNo, innerData.getPlainText(), outerData.getPlainText(), innerResult, outerResult, causeStackTrace, causeMessage);
        return "";
    }

    @Override
    public long updateRequestStatus(InterfaceContext context, WriteModeEnum writeOrderMode, String requestNo, OrderStatusEnum expectOrderStatus, OrderStatusEnum orderStatus) {
        log.error("request no:{}, expect status:{} target status:{}", requestNo, expectOrderStatus, orderStatus);
        return 0;
    }

    @Override
    public long updateRequestStatus(InterfaceContext context, WriteModeEnum writeOrderMode, String requestNo, OrderStatusEnum expectOrderStatus, OrderStatusEnum orderStatus, String responseNo) {
        log.error("request no:{}, expect status:{} target status:{}, response no：{}", requestNo, expectOrderStatus, orderStatus, responseNo);
        return 0;
    }

    @Override
    public String queryRequestBySessionId(InterfaceContext context, String sessionId, OrderStatusEnum expectOrderStatus) {
        return "";
    }
}
