package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.*;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.*;
import com.rnkrsoft.platform.service.security.DefaultSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 * 异步情况处理请求记录
 */
@Slf4j
class OuterAsyncRequestInterfaceEngine implements InterfaceEngine {
    @Override
    @Transactional(propagation = Propagation.NEVER)
    public boolean execute(InterfaceContext context) {
        Date createDate = new Date();
        Date lastUpdateDate = new Date();
        ErrorContextFactory.instance().reset();
        InterfaceDefinition interfaceDefinition = context.getInterfaceDefinition();
        InterfaceSecurity interfaceSecurity = context.getInterfaceSecurity();
        InterfaceData outerInput = context.getOuterInput();
        InterfaceData innerInput = context.getInnerInput();
        InterfaceData innerOutput = context.getInnerOutput();
        InterfaceData outerOutput = context.getOuterOutput();
        InterfaceResult outerResult = context.getOuterResult();
        InterfaceResult innerResult = context.getInnerResult();
        MessageService messageService = interfaceDefinition.getOrderMessageService();
        //
        //2.根据安全服务加密，签字
        SecurityService securityService = interfaceSecurity.getSecurityService();
        if (securityService == null) {
            securityService = new DefaultSecurityService();
        }
        if (interfaceSecurity.getFirstSignSecondEncrypt()) {
            if (!securityService.sign(context, innerOutput)) {
                outerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.encrypt(context, innerOutput)) {
                outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        } else {
            if (!securityService.encrypt(context, innerOutput)) {
                outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.sign(context, innerOutput)) {
                outerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        }
        if (innerOutput.getCipherText() == null || innerOutput.getCipherText().isEmpty()) {
            outerResult.setEnum(InterfaceRspCode.ENCRYPT_DATA_IS_NULL);
            innerResult.setEnum(InterfaceRspCode.ENCRYPT_DATA_IS_NULL);
            messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //3.将innerData ---> outerData进行转换
        ConvertService convertService = interfaceDefinition.getConvertService();
        if (convertService != null) {
            if (!convertService.code(context)) {
                outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            } else {
                //进行数据检查
                if (outerOutput.getCipherText() == null) {
                    outerResult.setEnum(InterfaceRspCode.ENCRYPT_DATA_IS_NULL);
                    innerResult.setEnum(InterfaceRspCode.ENCRYPT_DATA_IS_NULL);
                    messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                    messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                    return false;
                }
            }
        } else {
            outerOutput.setCipherText(innerOutput.getCipherText());
        }
        //5.获取同步发送服务
        SyncSendService syncSendService = interfaceDefinition.getSyncSendService();
        try {
            if (!syncSendService.send(context)) {//如果发送失败
                outerResult.setEnum(InterfaceRspCode.SYNC_SEND_SERVICE_HAPPENS_ERROR);
                innerResult.setEnum(InterfaceRspCode.SYNC_SEND_SERVICE_HAPPENS_ERROR);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        } catch (Exception e) {
            log.error("sync send happens error!", e);
            outerResult.setEnum(InterfaceRspCode.SYNC_SEND_SERVICE_HAPPENS_ERROR);
            innerResult.setEnum(InterfaceRspCode.SYNC_SEND_SERVICE_HAPPENS_ERROR);
            messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //6.进行外部数据解密和验签
        if (interfaceSecurity.getFirstSignSecondEncrypt()) {
            if (!securityService.verify(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }

            if (!securityService.decrypt(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }

        } else {
            if (!securityService.decrypt(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.verify(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        }
        if (outerInput.getPlainText() == null) {
            outerResult.setEnum(InterfaceRspCode.DECRYPT_DATA_IS_NULL);
            innerResult.setEnum(InterfaceRspCode.DECRYPT_DATA_IS_NULL);
            messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //7.进行内部数据映射转换
        if (convertService != null) {
            if (!convertService.code(context)) {
                outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_HAPPENS_ERROR);
                innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_HAPPENS_ERROR);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            } else {
                //进行数据检查
                if (innerInput.getPlainText() == null) {
                    outerResult.setEnum(InterfaceRspCode.DECRYPT_DATA_IS_NULL);
                    innerResult.setEnum(InterfaceRspCode.DECRYPT_DATA_IS_NULL);
                    messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                    messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                    return false;
                }
            }
        } else {
            innerInput.setPlainText(outerInput.getPlainText());
        }
        messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerOutput, outerOutput, OrderStatusEnum.SENT, createDate, lastUpdateDate);
        messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RETURN, innerInput, outerInput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
        return true;
    }
}
