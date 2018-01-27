package com.rnkrsoft.platform.jdbc.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.platform.jdbc.entity.ConfigureUserRoutingEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by rnkrsoft.com on 2018/10/7.
 */
public interface ConfigureUserRoutingDAO extends JdbcMapper<ConfigureUserRoutingEntity, String>{
    /**
     * 根据用户设备识别码查询路由信息
     * @param uic 用户识别码
     * @return
     */
    ConfigureUserRoutingEntity selectByUic(@Param("uic")String uic);

    /**
     * 更具设备类型和应用版本号查询路由信息
     * @param deviceType 设备类型
     * @param appVersion 应用版本号
     * @return
     */
    ConfigureUserRoutingEntity selectByDeviceTypeAndAppVersion(@Param("deviceType")String deviceType,@Param("appVersion") String appVersion);

    /**
     * 根据应用版本号查询路由信息
     * @param appVersion 应用版本号
     * @return
     */
    ConfigureUserRoutingEntity selectByAppVersion(@Param("appVersion") String appVersion);
}
