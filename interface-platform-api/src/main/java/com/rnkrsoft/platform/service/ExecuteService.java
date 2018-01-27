package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 * 执行服务
 */
public interface ExecuteService {
    /**
     * 执行服务
     * @param context 上下文
     * @return 执行成功返回true，否则返回false
     * @throws Throwable 抛出异常
     */
    boolean execute(InterfaceContext context) throws Throwable;
}
