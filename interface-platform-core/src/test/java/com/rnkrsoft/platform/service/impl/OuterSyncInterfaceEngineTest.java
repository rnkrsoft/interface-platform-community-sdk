package com.rnkrsoft.platform.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.demo.domains.DemoRequest;
import com.rnkrsoft.platform.demo.domains.DemoResponse;
import com.rnkrsoft.platform.service.InterfaceDefinitionService;
import com.rnkrsoft.platform.service.InterfaceEngine;
import com.rnkrsoft.platform.service.MessageService;
import com.rnkrsoft.platform.service.SyncSendService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 */
@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:OuterInterfaceEngineTest.xml")
public class OuterSyncInterfaceEngineTest extends DataSourceTest {
    @Autowired
    MessageService messageService;

    @Autowired(required = false)
    SyncSendService syncSendService;

    @Autowired
    InterfaceDefinitionService interfaceDefinitionService;

    Gson GSON = new GsonBuilder().serializeNulls().create();

    @Test
    public void testExecute() throws Exception {
        System.out.println(GSON.toJson(new DemoResponse()));

        for (int i = 0; i < 100; i++) {
            InterfaceEngine outerEngine = new OuterSyncInterfaceEngine();
            InterfaceContext ctx = InterfaceContext.builder()
                    .requestNo(UUID.randomUUID().toString())
                    .responseNo(UUID.randomUUID().toString())
                    .build();
            InterfaceData innerOutput = ctx.getInnerOutput();
            ctx.getInterfaceSecurity().setPassword("1234567");
            InterfaceDefinition interfaceDefinition = ctx.getInterfaceDefinition();
            innerOutput.setObject(new DemoRequest());
            interfaceDefinition.setChannel("sync_outer");
            interfaceDefinition.setTxNo("001");
            interfaceDefinition.setVersion("1");
            interfaceDefinition.setExecuteService(new SpringBeanExecuteService());
            interfaceDefinition.setSyncSendService(syncSendService);
            interfaceDefinition.setOrderMessageService(messageService);
            if (interfaceDefinitionService.setting(ctx)) {
                if (outerEngine.execute(ctx)) {
                    System.out.println(ctx.getInnerInput().getObject());
                }
            }
        }
    }
}