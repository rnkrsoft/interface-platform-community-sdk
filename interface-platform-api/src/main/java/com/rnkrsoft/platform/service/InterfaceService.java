package com.rnkrsoft.platform.service;


import com.rnkrsoft.platform.domains.Request;
import com.rnkrsoft.platform.domains.Response;

/**
 * Created by rnkrsoft.com on 2018/5/24.
 * 接口服务
 */
public interface InterfaceService {
    FlowLimitingService getFlowLimitingService();
    /**
     * 执行接口服务
     * @param request 请求对象
     * @return 应答对象
     */
    Response invoke(Request request);
}
