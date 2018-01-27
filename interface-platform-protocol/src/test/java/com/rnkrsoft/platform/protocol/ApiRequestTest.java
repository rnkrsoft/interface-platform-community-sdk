package com.rnkrsoft.platform.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/26.
 */
public class ApiRequestTest {
    Gson gson = new GsonBuilder().serializeNulls().create();
    @Test
    public void testSetClientType() throws Exception {
        ApiRequest request = new ApiRequest();
        request.setSessionId(UUID.randomUUID().toString());
        request.setChannel("CAR_MANAGE");
        request.setTxNo("001");
        request.setVersion("1");
        request.setUic("123");
        request.setUid("1234");
        request.setLat(1.2D);
        request.setLng(1.4D);
        request.setData("{}");
        request.setSign("xxxxxxxxxxxxxxx");
        request.setTimestamp("201806290849001");
        request.setToken("xxxxxxxxxxxxxxxxxxx");
        System.out.println(gson.toJson(request));
    }
}