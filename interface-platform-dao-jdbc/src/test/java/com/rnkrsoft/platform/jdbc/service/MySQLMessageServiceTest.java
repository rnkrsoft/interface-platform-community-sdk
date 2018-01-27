package com.rnkrsoft.platform.jdbc.service;

import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.jdbc.entity.InterfaceRequestEntity;
import com.rnkrsoft.platform.service.MessageService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:MySQLMessageServiceTest.xml")
public class MySQLMessageServiceTest extends DataSourceTest {

    @Test
    @CreateTable(entities = {InterfaceRequestEntity.class}, prefix = "tb")
    public void testCreateRequestSync() throws Exception {
        MessageService messageService = getBean(MessageService.class);
        String sessionId = UUID.randomUUID().toString();
        InterfaceContext context = InterfaceContext.builder()
                .sessionId(sessionId)
                .uic("uid-test")
                .uid("18502360568")
                .deviceManufacturer("xiaomi")
                .deviceModel("xiaomi note")
                .deviceType("Android")
                .osVersion("6.5")
                .macAddress("44-45-53-54-00-00")
                .lat(1.223)
                .lng(2.364)
                .clientIp("127.0.0.1")
                .clientPort(80)
                .build();
        context.getInterfaceDefinition().setChannel("test-channel");
        context.getInterfaceDefinition().setTxNo("010");
        context.getInterfaceDefinition().setVersion("1.0.0");
        String requestNo = messageService.createRequest(context, WriteModeEnum.SYNC, UUID.randomUUID().toString(), "", InterfaceData.builder().plainText("xxxxx").build(), InterfaceData.builder().cipherText("sdasdas").build(), OrderStatusEnum.SENT, new Date(), new Date());

    }
}