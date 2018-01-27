package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.domains.IpLocationRequest;
import com.rnkrsoft.platform.domains.IpLocationResponse;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;

/**
 * Created by rnkrsoft.com on 2018/12/2.
 */
@ApidocService("IP信息服务")
public interface IpInfoService {
    @ApidocInterface("IP定位")
    IpLocationResponse ipLocation(IpLocationRequest request);
}
