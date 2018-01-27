package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 */
public interface InterfaceEngine {
    /**
     * 执行引擎操作
     * @param context 上下文
     * @return 执行成功返回真
     */
    boolean execute(InterfaceContext context);
}
