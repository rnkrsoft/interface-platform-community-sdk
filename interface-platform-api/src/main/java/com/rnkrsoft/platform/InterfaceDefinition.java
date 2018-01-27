package com.rnkrsoft.platform;

import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.enums.InterfaceStageEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.service.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Method;

import static com.rnkrsoft.platform.InterfacePlatformConstants.PUBLIC_CHANNEL;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 */
@Data
@Builder
@ToString
public class InterfaceDefinition {
    /**
     * 通道号
     */
    String channel;
    /**
     * 通道名称
     */
    String channelName;
    /**
     * 交易码
     */
    String txNo;
    /**
     * 版本号
     */
    String version;
    /**
     * 网关地址，在同步外发时使用
     */
    String gatewayUrl;
    /**
     * 是否为异步接口
     */
    boolean async;
    /**
     * HTTP同步外发时超时秒数
     */
    int httpTimeoutSecond;
    /**
     * 接口方向类型
     */
    InterfaceDirectionEnum interfaceDirection;
    /**
     * 接口类型
     */
    InterfaceStageEnum interfaceStage;
    /**
     * 是否记录通讯信息
     */
    boolean writeMessage = true;
    /**
     * 写订单模式
     */
    WriteModeEnum writeMode;
    /**
     * 接口服务类
     */
    String interfaceClassName;
    /**
     * 接口方法名
     */
    String interfaceMethodName;
    /**
     * 接口方法对象
     */
    Method interfaceMethod;
    /**
     * 转换服务
     */
    ConvertService convertService;
    /**
     * 执行服务
     */
    ExecuteService executeService;
    /**
     * 同步发送服务
     */
    SyncSendService syncSendService;
    /**
     * 订单信息服务
     */
    MessageService orderMessageService;
    /**
     * TOKEN服务
     */
    TokenService tokenService;

    public InterfaceDefinition() {
        this.interfaceDirection = InterfaceDirectionEnum.INNER;
    }

    public InterfaceDefinition(
            String channel,
            String channelName,
            String txNo,
            String version,
            String gatewayUrl,
            boolean async,
            int httpTimeoutSecond,
            InterfaceDirectionEnum interfaceDirection,
            InterfaceStageEnum interfaceStage,
            boolean writeMessage,
            WriteModeEnum writeMode,
            String interfaceClassName,
            String interfaceMethodName,
            Method interfaceMethod,
            ConvertService convertService,
            ExecuteService executeService,
            SyncSendService syncSendService,
            MessageService orderMessageService,
            TokenService tokenService) {
        this.channel = channel == null ? PUBLIC_CHANNEL : channel;
        this.channelName = channelName == null ? "公共通道" : channelName;
        this.txNo = txNo;
        this.version = version;
        this.gatewayUrl = gatewayUrl;
        this.async = async;
        this.httpTimeoutSecond = httpTimeoutSecond;
        this.interfaceDirection = interfaceDirection == null ? InterfaceDirectionEnum.INNER : interfaceDirection;
        this.interfaceStage = interfaceStage == null ? InterfaceStageEnum.REQUEST : interfaceStage;
        this.writeMessage = writeMessage;
        this.writeMode = writeMode == null ? WriteModeEnum.SYNC : writeMode;
        this.interfaceClassName = interfaceClassName;
        this.interfaceMethodName = interfaceMethodName;
        this.interfaceMethod = interfaceMethod;
        this.convertService = convertService;
        this.executeService = executeService;
        this.syncSendService = syncSendService;
        this.orderMessageService = orderMessageService;
        this.tokenService = tokenService;
    }
}
