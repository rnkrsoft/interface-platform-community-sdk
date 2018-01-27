package com.rnkrsoft.platform.mongo.service;

import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.framework.test.SpringTest;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceResult;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import com.rnkrsoft.platform.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/27.
 */
@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:MongoMessageServiceTest.xml")
public class MongoMessageServiceTest extends SpringTest {
    @Test
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
        String requestNo = messageService.createRequest(context, WriteModeEnum.SYNC, "", UUID.randomUUID().toString(), InterfaceData.builder().plainText("xxxxx").build(), InterfaceData.builder().cipherText("sdasdas").build(), OrderStatusEnum.SENT, new Date(), new Date());
        String requestNo1 = messageService.queryRequestBySessionId(context, sessionId, OrderStatusEnum.SENT);
        String responseNo = messageService.createResponse(InterfaceContext.builder().build(), WriteModeEnum.SYNC, UUID.randomUUID().toString(), requestNo1, RspTypeEnum.SYNC_RESPONSE, InterfaceData.builder().plainText("xxxxx").build(), InterfaceData.builder().cipherText("sdasdas").build(), InterfaceResult.builder().code("xxx").desc("测试").build(), InterfaceResult.builder().code("xxx").desc("测试").build(), new Date(), new Date());
        System.out.println("------------" + requestNo1);
        long updateCount = messageService.updateRequestStatus(InterfaceContext.builder().build(), WriteModeEnum.SYNC, requestNo1, OrderStatusEnum.SENT, OrderStatusEnum.SUCCESS, responseNo);
        Assert.assertEquals(1, updateCount);
    }

    @Test
    public void testCreateRequestAsync() throws Exception {

    }

    @Test
    public void testCreateResponseSync() throws Exception {

    }

    @Test
    public void testCreateResponseAsync() throws Exception {

    }

    @Test
    public void testCreateResponseSync1() throws Exception {

    }

    @Test
    public void testCreateResponseAsync1() throws Exception {

    }

    @Test
    public void testUpdateRequestStatusSync() throws Exception {

    }

    @Test
    public void testUpdateRequestStatusAsync() throws Exception {

    }

    @Test
    public void testUpdateRequestStatusSync1() throws Exception {

    }

    @Test
    public void testUpdateRequestStatusAsync1() throws Exception {

    }

    @Test
    public void testQueryRequestBySessionId() throws Exception {

    }
}