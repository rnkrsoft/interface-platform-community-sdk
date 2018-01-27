package com.rnkrsoft.platform.proxy;

import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.platform.demo.domains.DemoRequest;
import com.rnkrsoft.platform.demo.domains.DemoResponse;
import com.rnkrsoft.platform.demo.service.DemoService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by Administrator on 2018/7/25.
 */
@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:OuterInterfaceEngineTest.xml")
public class InterfaceSenderProxyTest extends DataSourceTest {
    @Test
    public void testInvoke() throws Exception {
        long now = System.currentTimeMillis();
        System.out.println("----------------------------");
        InterfaceSenderProxyFactory<DemoService> factory = new InterfaceSenderProxyFactory(DemoService.class);
        DemoService demoService = factory.newInstance();
        DemoRequest demoRequest = new DemoRequest();
        demoRequest.setName("xxx");
        DemoResponse response = demoService.demo(demoRequest);
        System.out.println(response);
        System.out.println(System.currentTimeMillis() - now);
    }
}