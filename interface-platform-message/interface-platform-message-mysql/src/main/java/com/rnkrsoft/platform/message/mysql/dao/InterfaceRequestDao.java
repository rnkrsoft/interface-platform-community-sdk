package com.rnkrsoft.platform.message.mysql.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.platform.message.mysql.entity.InterfaceRequestEntity;
import org.apache.ibatis.annotations.Param;

/**
 * rnkrsoft.com 框架自动生成!
 */
public interface InterfaceRequestDao extends JdbcMapper<InterfaceRequestEntity, String> {
    /**
     * 更新请求订单订单状态和应答信息
     * @param requestNo 请求订单号
     * @param expectOrderStatus 期望订单状态
     * @param orderStatus 订单状态
     * @param responseNo 应答订单号
     * @return
     */
    int updateStatusAndRspNo(@Param("requestNo") String requestNo,
                      @Param("expectOrderStatus") Integer expectOrderStatus,
                      @Param("orderStatus") Integer orderStatus,
                      @Param("responseNo") String responseNo);

    /**
     * 更新请求订单订单状态和应答信息
     * @param requestNo 请求订单号
     * @param expectOrderStatus 期望订单状态
     * @param orderStatus 订单状态
     * @return
     */
    int  updateStatus(@Param("requestNo") String requestNo,
                      @Param("expectOrderStatus") Integer expectOrderStatus,
                      @Param("orderStatus") Integer orderStatus);
}
