package com.rnkrsoft.platform;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 */
@Data
@Builder
public class InterfaceContext {
    /**
     * 安全信息配置
     */
    final InterfaceSecurity interfaceSecurity = new InterfaceSecurity();
    /**
     * 接口定义信息
     */
    final InterfaceDefinition interfaceDefinition = new InterfaceDefinition();
    /**
     * 外部数据
     */
    final InterfaceData outerInput = new InterfaceData();
    /**
     * 内部数据
     */
    final InterfaceData innerInput = new InterfaceData();
    /**
     * 内部数据
     */
    final InterfaceData innerOutput = new InterfaceData();
    /**
     * 外部数据
     */
    final InterfaceData outerOutput = new InterfaceData();
    /**
     * 内部执行结果
     */
    final InterfaceResult innerResult = new InterfaceResult();
    /**
     * 外部执行结果
     */
    final InterfaceResult outerResult = new InterfaceResult();
    /**
     * 会话ID，在日志中记录有,在用户APP产生
     */
    String sessionId;
    /**
     * 请求订单号
     */
    String requestNo = UUID.randomUUID().toString();
    /**
     * 应答订单号
     */
    String responseNo = UUID.randomUUID().toString();
    /**
     * 令牌
     */
    String token;
    /**
     * 客户端IP地址
     */
    String clientIp;
    /**
     * 客户端的端口号
     */
    int clientPort;
    /**
     * 设备厂商 例如,xiaomi,apple
     */
    String deviceManufacturer;
    /**
     * 设备型号 例如 xiaomi note, iphone 6s
     */
    String deviceModel;
    /**
     * MAC地址 例如 44-45-53-54-00-00
     */
    String macAddress;
    /**
     * 设备类型 例如 iOS,Android,H5
     */
    String deviceType;
    /**
     * 操作系统版本 例如 iOS 8
     */
    String osVersion;
    /**
     * 应用版本 例如 3.1.2
     */
    String appVersion;
    /**
     * 用户ID
     */
    String uid;
    /**
     * 用户名
     */
    String uname;
    /**
     * 用户识别码
     */
    String uic;
    /**
     * 时间戳
     */
    String timestamp;
    /**
     * 经度
     */
    Double lat;
    /**
     * 纬度
     */
    Double lng;

    public InterfaceContext(String sessionId, String requestNo, String responseNo, String token, String clientIp, int clientPort, String deviceManufacturer, String deviceModel, String macAddress, String deviceType, String osVersion, String appVersion, String uid, String uname, String uic, String timestamp, Double lat, Double lng) {
        this.sessionId = sessionId;
        this.requestNo = requestNo == null ? UUID.randomUUID().toString() : requestNo;
        this.responseNo =  responseNo == null ? UUID.randomUUID().toString() : responseNo;
        this.token = token;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.deviceManufacturer = deviceManufacturer;
        this.deviceModel = deviceModel;
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.uid = uid;
        this.uname = uname;
        this.uic = uic;
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
    }
}
