package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.service.SyncSendService;

/**
 * Created by Administrator on 2018/7/25.
 */
public class MockSyncSendService implements SyncSendService{
    @Override
    public boolean send(InterfaceContext context) {
        context.getOuterInput().setCipherText("{\"age\":125,\"rspCode\":\"0000\",\"rspDesc\":\"成功\"}");
        return true;
    }
}
