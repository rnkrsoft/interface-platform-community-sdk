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
 * 例如输入微信数据
 * 同步执行，如果引擎返回成功，则去尝试执行处理函数；如果引擎执行失败，则抛出异常
 */
@Slf4j
class OuterAsyncResponseInterfaceEngine implements InterfaceEngine {
    @Override
    @Transactional(propagation = Propagation.NEVER)
    public boolean execute(InterfaceContext context) {
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
        String requestNo = messageService.queryRequestBySessionId(context, context.getSessionId(), OrderStatusEnum.SENT);
        if (requestNo == null) {
            innerResult.setEnum(InterfaceRspCode.REQUEST_DATA_IS_NULL);
            outerResult.setEnum(InterfaceRspCode.REQUEST_DATA_IS_NULL);
            return false;
        }
        context.setRequestNo(requestNo);
        //5. 根据安全服务验签和解密
        SecurityService securityService = interfaceSecurity.getSecurityService();
        if (securityService == null) {
            securityService = new DefaultSecurityService();
        }
        if (interfaceSecurity.getFirstSignSecondEncrypt()) {
            if (!securityService.verify(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }

            if (!securityService.decrypt(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }

        } else {
            if (!securityService.decrypt(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.verify(context, outerInput)) {
                outerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        }
        if (outerInput.getPlainText() == null) {
            outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
            innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //6. 调用转换服务进行 从outerMessage-->innerObject
        ConvertService convertService = interfaceDefinition.getConvertService();
        if (convertService != null) {
            if (!convertService.code(context)) {
                outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_HAPPENS_ERROR);
                innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_HAPPENS_ERROR);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            } else {
                //进行数据检查
                if (innerInput.getPlainText() == null) {
                    outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                    return false;
                }
            }
        } else {
            innerInput.setPlainText(outerInput.getPlainText());
        }
        //8. 根据服务类型（Local,SpringBean,Dubbo）调用服务，发生错误创建应答记录，并更新请求记录
        ExecuteService executeService = interfaceDefinition.getExecuteService();
        long result = messageService.updateRequestStatus(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), OrderStatusEnum.SENT, OrderStatusEnum.SUCCESS, context.getResponseNo());
        if (result != 1) {
            outerResult.setEnum(InterfaceRspCode.UPDATE_REQUEST_HAPPENS_ERROR);
            innerResult.setEnum(InterfaceRspCode.UPDATE_REQUEST_HAPPENS_ERROR);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        try {
            if (!executeService.execute(context)) {
                log.debug("execute interface happen error!");
                String responseNo = messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                messageService.updateRequestStatus(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), OrderStatusEnum.SENT, OrderStatusEnum.FAIL, responseNo);
                return false;
            }
        } catch (Throwable e) {
            outerResult.setEnum(InterfaceRspCode.INTERFACE_HAPPEN_UNKNOWN_ERROR);
            innerResult.setEnum(InterfaceRspCode.INTERFACE_HAPPEN_UNKNOWN_ERROR);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //10. 根据安全服务进行加密和签字
        if (convertService != null) {
            if (!convertService.code(context)) {
                outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            } else {
                //进行数据检查
                if (innerOutput.getPlainText() == null) {
                    outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                    return false;
                }
            }
        } else {
            outerOutput.setPlainText(innerOutput.getPlainText());
        }
        if (interfaceSecurity.getFirstSignSecondEncrypt()) {
            if (!securityService.sign(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.encrypt(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        } else {
            if (!securityService.encrypt(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.sign(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        }
        if (outerOutput.getCipherText() == null || outerOutput.getCipherText().isEmpty()) {
            outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
            innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //11. 创建应答记录
        String responseNo = messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.ASYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
        context.setResponseNo(responseNo);
        return true;
    }
}
