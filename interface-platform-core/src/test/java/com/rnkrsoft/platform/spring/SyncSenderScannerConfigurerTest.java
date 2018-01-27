package com.rnkrsoft.platform.spring;

import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.platform.demo.domains.DemoRequest;
import com.rnkrsoft.platform.demo.domains.DemoResponse;
import com.rnkrsoft.platform.sync.DemoSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 */
@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:SyncSenderScannerConfigurerTest.xml")
public class SyncSenderScannerConfigurerTest extends DataSourceTest {
    @Autowired
    DemoSender demoSender;
    @Test
    public void testPostProcessBeanDefinitionRegistry() throws Exception {
        DemoResponse response = demoSender.demo(new DemoRequest());
        System.out.println(response);
    }
}