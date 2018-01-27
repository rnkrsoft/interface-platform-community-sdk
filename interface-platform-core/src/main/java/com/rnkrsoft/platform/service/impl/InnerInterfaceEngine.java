package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.platform.*;
import com.rnkrsoft.platform.domains.ValidateTokenRequest;
import com.rnkrsoft.platform.domains.ValidateTokenResponse;
import com.rnkrsoft.platform.enums.BaseRspCodeEnum;
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
 * Created by rnkrsoft.com on 2018/6/23.
 * 对外暴露服务，可以调用到内部服务的引擎实现
 */
@Slf4j
class InnerInterfaceEngine implements InterfaceEngine {

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
        TokenService tokenService = interfaceDefinition.getTokenService();
        //3. 根据交易定义检查是否验证TOKEN
        //如果接口校验TOKEN
        if (interfaceSecurity.getValidateToken()) {
            if (interfaceDefinition.getTokenService() == null) {
                log.error("TOKEN服务'{}'未配置", TokenService.class);
                outerResult.setEnum(InterfaceRspCode.TOKEN_SERVICE_NOT_EXISTS);
                innerResult.setEnum(InterfaceRspCode.TOKEN_SERVICE_NOT_EXISTS);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            ValidateTokenResponse validateTokenResponse = tokenService.validateToken(ValidateTokenRequest.builder().token(context.getToken()).build());
            if (BaseRspCodeEnum.valueOfCode(validateTokenResponse.getRspCode()) != BaseRspCodeEnum.SUCCESS) {//如果校验TOKEN不为成功
                log.error("验证TOKEN '{}' 失败!", context.getToken());
                outerResult.setEnum(InterfaceRspCode.TOKEN_ILLEGAL);
                innerResult.setEnum(InterfaceRspCode.TOKEN_ILLEGAL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            context.setUid(validateTokenResponse.getUserId());
            context.setUname(validateTokenResponse.getUserName());
            if (log.isDebugEnabled()) {
                log.debug("验证TOKEN成功，token:'{}', uid:'{}',uname'{}'", context.getToken(), validateTokenResponse.getUserId(), validateTokenResponse.getUserName());
            }
        } else {
            context.setUid("0");
        }
        //5. 根据安全服务验签和解密
        SecurityService securityService = interfaceSecurity.getSecurityService();
        if (securityService == null) {
            securityService = new DefaultSecurityService();
        }
        if (interfaceSecurity.getFirstSignSecondEncrypt()) {
            if (!securityService.verify(context, outerInput)) {
                log.error("验签发生错误!");
                outerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }

            if (!securityService.decrypt(context, outerInput)) {
                log.error("解密发生错误!");
                outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }

        } else {
            if (!securityService.decrypt(context, outerInput)) {
                log.error("解密发生错误!");
                outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.verify(context, outerInput)) {
                log.error("验签发生错误!");
                outerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.VERIFY_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        }
        if (outerInput.getPlainText() == null) {
            log.error("解密发生错误!");
            outerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
            innerResult.setEnum(InterfaceRspCode.DECRYPT_HAPPENS_FAIL);
            messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        //6. 调用转换服务进行 从outerMessage-->innerObject
        ConvertService convertService = interfaceDefinition.getConvertService();
        if (convertService != null) {
            if (!convertService.code(context)) {
                outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_HAPPENS_ERROR);
                innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_HAPPENS_ERROR);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            } else {
                //进行数据检查
                if (innerInput.getPlainText() == null) {
                    outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                    messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                    return false;
                }
            }
        } else {
            innerInput.setPlainText(outerInput.getPlainText());
        }

        //7. 创建请求记录
        //8. 根据服务类型（Local,SpringBean,Dubbo）调用服务，发生错误创建应答记录，并更新请求记录
        ExecuteService executeService = interfaceDefinition.getExecuteService();
        try {
            if (!executeService.execute(context)) {
                //执行接口失败
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        } catch (Throwable e) {
            log.error(MessageFormatter.format("执行接口'{}:{}:{}'失败! ", interfaceDefinition.getChannel(),interfaceDefinition.getTxNo(), interfaceDefinition.getVersion()), e);
            outerResult.setEnum(InterfaceRspCode.INTERFACE_HAPPEN_UNKNOWN_ERROR);
            innerResult.setEnum(InterfaceRspCode.INTERFACE_HAPPEN_UNKNOWN_ERROR);
            messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(), innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }


        //10. 根据安全服务进行加密和签字
        if (convertService != null) {
            if (!convertService.code(context)) {
                log.error("执行接口'{}:{}:{}'应达数据转换发生失败! ", interfaceDefinition.getChannel(), interfaceDefinition.getTxNo(), interfaceDefinition.getVersion());
                outerResult.setEnum(InterfaceRspCode.CONVERT_RESPONSE_DATA_FAIL);
                innerResult.setEnum(InterfaceRspCode.CONVERT_RESPONSE_DATA_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            } else {
                //进行数据检查
                if (innerOutput.getPlainText() == null) {
                    outerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    innerResult.setEnum(InterfaceRspCode.DATA_CONVERT_SERVICE_EXISTS_ERROR);
                    messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                    messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
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
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.encrypt(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        } else {
            if (!securityService.encrypt(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
            if (!securityService.sign(context, outerOutput)) {
                outerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                innerResult.setEnum(InterfaceRspCode.SIGN_HAPPENS_FAIL);
                messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
                messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
                return false;
            }
        }
        if (outerOutput.getCipherText() == null || outerOutput.getCipherText().isEmpty()) {
            outerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
            innerResult.setEnum(InterfaceRspCode.ENCRYPT_HAPPENS_FAIL);
            messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.FAIL, createDate, lastUpdateDate);
            messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
            return false;
        }
        messageService.createRequest(context, interfaceDefinition.getWriteMode(), context.getRequestNo(), context.getResponseNo(),  innerInput, outerInput, OrderStatusEnum.SUCCESS, createDate, lastUpdateDate);
        messageService.createResponse(context, interfaceDefinition.getWriteMode(), context.getResponseNo(), context.getRequestNo(), RspTypeEnum.SYNC_RESPONSE, innerOutput, outerOutput, context.getInnerResult(), context.getOuterResult(), new Date(), new Date());
        return true;
    }
}
