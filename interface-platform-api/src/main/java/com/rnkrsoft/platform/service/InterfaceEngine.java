package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 * 接口引擎，无论是内部接口还是外部接口均执行这个接口的实现服务
 */
public interface InterfaceEngine {
    /**
     * 执行引擎操作
     * @param context 上下文
     * @return 执行成功返回真
     */
    boolean execute(InterfaceContext context);
}
