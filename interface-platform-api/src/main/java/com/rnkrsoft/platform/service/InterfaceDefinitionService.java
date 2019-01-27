package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/25.
 * 接口定义服务
 */
public interface InterfaceDefinitionService {
    /**
     * 设置接口定义信息
     * @param context 对象定义对象
     */
    boolean setting(InterfaceContext context);
}
