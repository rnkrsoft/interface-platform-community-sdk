package com.rnkrsoft.platform.service;


import com.rnkrsoft.platform.protocol.ApiRequest;
import com.rnkrsoft.platform.protocol.ApiResponse;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;

/**
 * Created by rnkrsoft.com on 2018/5/24.
 */
@ApidocService("接口服务")
public interface InterfaceService {
    @ApidocInterface("调用服务")
    ApiResponse invoke(ApiRequest request, String clientIp, int clientPort);
}
