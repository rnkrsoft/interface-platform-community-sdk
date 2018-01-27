package com.rnkrsoft.platform.demo.service;

import com.rnkrsoft.platform.annotation.InterfaceSender;
import com.rnkrsoft.platform.demo.domains.DemoRequest;
import com.rnkrsoft.platform.demo.domains.DemoResponse;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;

/**
 * Created by Administrator on 2018/6/19.
 */
@InterfaceSender(channel = "user_core", syncSendServiceClassName = "mockSyncSendService")
@ApidocService(value = "演示服务", version = "1")
public interface DemoService {
    @ApidocInterface(value = "演示", name = "001")
    DemoResponse demo(DemoRequest request);
}
