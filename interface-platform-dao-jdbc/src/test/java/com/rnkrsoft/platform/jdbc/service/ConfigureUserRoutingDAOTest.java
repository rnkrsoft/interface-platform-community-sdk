package com.rnkrsoft.platform.jdbc.service;

import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.DataSourceTest;
import com.rnkrsoft.framework.test.DataSourceType;
import com.rnkrsoft.platform.jdbc.dao.ConfigureUserRoutingDAO;
import com.rnkrsoft.platform.jdbc.entity.ConfigureUserRoutingEntity;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@DataSource(DataSourceType.MySQL)
@ContextConfiguration(locations = "classpath*:MySQLMessageServiceTest.xml")
public class ConfigureUserRoutingDAOTest extends DataSourceTest {

    @Test
    @CreateTable(entities = {ConfigureUserRoutingEntity.class}, prefix = "tb")
    public void testCreateRequestSync() throws Exception {
        ConfigureUserRoutingDAO configureUserRoutingDAO = getBean(ConfigureUserRoutingDAO.class);
        configureUserRoutingDAO.selectAll();
    }
}