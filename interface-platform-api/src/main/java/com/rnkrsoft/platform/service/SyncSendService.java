package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 * 同步发送服务
 */
public interface SyncSendService {
    /**
     * 同步发送服务
     * @param context 上下文
     * @return 是否处理
     */
    boolean send(InterfaceContext context);
}
