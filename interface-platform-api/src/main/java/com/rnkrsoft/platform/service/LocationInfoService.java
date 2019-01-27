package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.domains.LocationRequest;
import com.rnkrsoft.platform.domains.LocationResponse;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;

/**
 * Created by rnkrsoft.com on 2018/12/2.
 */
@ApidocService("IP信息服务")
public interface LocationInfoService {
    @ApidocInterface("IP定位")
    LocationResponse location(LocationRequest request);
}
