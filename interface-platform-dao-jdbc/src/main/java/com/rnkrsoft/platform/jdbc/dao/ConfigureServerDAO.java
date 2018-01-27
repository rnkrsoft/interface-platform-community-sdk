package com.rnkrsoft.platform.jdbc.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.platform.jdbc.entity.ConfigureServerEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/10/7.
 */
public interface ConfigureServerDAO extends JdbcMapper<ConfigureServerEntity, String>{
    /**
     * 通过环境和通道列表查询服务器
     * @param env 环境
     * @param channels 通道列表
     * @return 服务器列表
     */
    List<ConfigureServerEntity> selectServerByEnvChannels(@Param("env") Integer env, @Param("channels")List<String> channels);
}
