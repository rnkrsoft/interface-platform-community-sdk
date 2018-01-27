package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.platform.jdbc.entity.InterfaceDefinitionEntity;
import com.rnkrsoft.platform.jdbc.entity.InterfaceRequestEntity;
import com.rnkrsoft.platform.jdbc.entity.InterfaceResponseEntity;
import com.rnkrsoft.platform.protocol.ApiRequest;
import com.rnkrsoft.platform.protocol.ApiResponse;
import com.rnkrsoft.platform.service.InterfaceService;
import com.rnkrsoft.utils.DateUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by Administrator on 2018/6/19.
 */
@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:InterfaceServiceImplTest.xml")
public class InterfaceServiceImplTest extends DataSourceTest{

    @Test
    @CreateTable(entities = {
            InterfaceDefinitionEntity.class,
            InterfaceRequestEntity.class,
            InterfaceResponseEntity.class
    }, prefix = "TB")
    public void testInvoke() throws Exception {
        InterfaceService apiService = getBean(InterfaceService.class);
        ApiResponse response = apiService.invoke(ApiRequest.builder()
                .sessionId(UUID.randomUUID().toString())
                .channel("sync_inner")
                .txNo("001")
                .version("1")
                .uic("xxxx")
                .uid("xxx")
                .timestamp(DateUtils.getTimestamp())
                .lat(0.01D)
                .lng(0.01D)
                .token("2dd1e1fa-3388-4a53-adf2-9a1b08854b0b")
                .sign("e5a031eebe6deb42ee418639d589b3537e9683c71ad2da386c9918fc1ea251da3cbc0566969bea1fa9cde4e1113b449d99fbd2b8ad6140cb978ee2bb32d45745")
                .data("{\"name\":\"this is a test\"}")
                .build(), "localhost", 80);
        System.out.println(response);
    }
}