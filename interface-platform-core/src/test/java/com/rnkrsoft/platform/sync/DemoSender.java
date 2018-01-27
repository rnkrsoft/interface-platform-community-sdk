package com.rnkrsoft.platform.sync;

import com.rnkrsoft.platform.annotation.InterfaceSender;
import com.rnkrsoft.platform.demo.domains.DemoRequest;
import com.rnkrsoft.platform.demo.domains.DemoResponse;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;

/**
 * Created by Administrator on 2018/7/25.
 */
@InterfaceSender(channel = "user_core", syncSendServiceClassName = "mockSyncSendService", async = true)
@ApidocService(value = "演示服务", version = "1")
public interface DemoSender {
    @ApidocInterface(value = "演示", name = "001")
    DemoResponse demo(DemoRequest request);
}
