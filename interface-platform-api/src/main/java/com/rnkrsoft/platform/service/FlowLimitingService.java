package com.rnkrsoft.platform.service;

import java.util.concurrent.TimeUnit;

/**
 * Created by rnkrsoft.com on 2018/10/12.
 * 流量限制服务，用于流量的控制
 */
public interface FlowLimitingService {
    /**
     * 检测在客户端地址，端口和通道号为条件进行限流
     * @param uid 用户号
     * @param uic 用户识别码
     * @param clientAddress 客户端地址
     * @param channel 通道号
     * @param maxThreshold 单位时间内最大访问数
     * @param timeUnit 单位时间
     * @return 如果未超过每分钟阈值，返回真，否则返回假
     */
    boolean detects(String uid, String uic, String clientAddress, String channel, int maxThreshold, TimeUnit timeUnit);
}
