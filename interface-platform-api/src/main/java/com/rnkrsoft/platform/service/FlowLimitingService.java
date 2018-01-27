package com.rnkrsoft.platform.service;

/**
 * Created by rnkrsoft.com on 2018/10/12.
 * 流量限制服务
 */
public interface FlowLimitingService {
    /**
     * 检测在客户端地址，端口和通道号为条件进行限流
     * @param uid 用户号
     * @param uic 用户识别码
     * @param clientAddress 客户端地址
     * @param channel 通道号
     * @param maxThresholdPreMin 每分钟最大阈值
     * @return 如果未超过每分钟阈值，返回真，否则返回假
     */
    boolean detects(String uid, String uic, String clientAddress, String channel, int maxThresholdPreMin);
}
