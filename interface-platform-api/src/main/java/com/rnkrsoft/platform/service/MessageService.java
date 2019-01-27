package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceResult;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;

import java.util.Date;


/**
 * Created by rnkrsoft.com on 2018/6/21.
 * 信息服务，用于对接口产生的数据进行持久化
 */
public interface MessageService {
    /**
     * 设置是否异步写入
     *
     * @param asyncWrite 是否异步写入
     */
    void setAsyncWrite(boolean asyncWrite);

    /**
     * 初始化消息服务
     */
    void init();

    /**
     * 创建请求订单数据
     *
     * @param context
     * @param writeOrderMode 写入模式
     * @param requestNo      请求订单号
     * @param responseNo     应答订单号
     * @param innerData      系统内侧报文
     * @param outerData      系统外侧报文
     * @param orderStatus    订单状态
     * @param createDate     创建日期
     * @param lastUpdateDate 更新日期
     * @return 请求订单号
     */
    String createRequest(InterfaceContext context,
                         WriteModeEnum writeOrderMode,
                         String requestNo,
                         String responseNo,
                         InterfaceData innerData,
                         InterfaceData outerData,
                         OrderStatusEnum orderStatus,
                         Date createDate,
                         Date lastUpdateDate);


    /**
     * 创建应答订单
     *
     * @param context
     * @param writeOrderMode 写入模式
     * @param responseNo     应答订单号
     * @param requestNo      请求订单号
     * @param rspType        应答类型
     * @param innerData      系统内侧报文
     * @param outerData      系统外侧报文
     * @param innerResult    系统内侧结果
     * @param outerResult    系统外侧结果
     * @param createDate     创建日期
     * @param lastUpdateDate 更新日期
     * @return 应答订单号
     */
    String createResponse(InterfaceContext context,
                          WriteModeEnum writeOrderMode,
                          String responseNo,
                          String requestNo,
                          RspTypeEnum rspType,
                          InterfaceData innerData,
                          InterfaceData outerData,
                          InterfaceResult innerResult,
                          InterfaceResult outerResult,
                          Date createDate,
                          Date lastUpdateDate);

    /**
     * 创建应答订单
     *
     * @param context
     * @param writeOrderMode  写入模式
     * @param responseNo      应答订单号
     * @param requestNo       请求订单号
     * @param rspType         应答类型
     * @param innerData       系统内侧报文
     * @param outerData       系统外侧报文
     * @param innerResult     系统内侧结果
     * @param outerResult     系统外侧结果
     * @param causeStackTrace 发生异常堆栈
     * @param causeMessage    异常信息
     * @param createDate      创建日期
     * @param lastUpdateDate  更新日期
     * @return 应答订单号
     */
    String createResponse(InterfaceContext context,
                          WriteModeEnum writeOrderMode,
                          String responseNo,
                          String requestNo,
                          RspTypeEnum rspType,
                          InterfaceData innerData,
                          InterfaceData outerData,
                          InterfaceResult innerResult,
                          InterfaceResult outerResult,
                          String causeStackTrace,
                          String causeMessage,
                          Date createDate,
                          Date lastUpdateDate);

    /**
     * 更新订单状态
     *
     * @param writeOrderMode    写入模式
     * @param requestNo         请求号
     * @param expectOrderStatus 期望订单状态
     * @param orderStatus       修改订单状态
     * @return 影响记录条数
     */
    long updateRequestStatus(InterfaceContext context,
                             WriteModeEnum writeOrderMode,
                             String requestNo,
                             OrderStatusEnum expectOrderStatus,
                             OrderStatusEnum orderStatus);

    /**
     * 更新订单状态和应答订单号
     *
     * @param writeOrderMode    写入模式
     * @param requestNo         请求号
     * @param expectOrderStatus 期望订单状态
     * @param orderStatus       修改订单状态
     * @param responseNo        应答订单号
     * @return 影响记录条数
     */
    long updateRequestStatus(InterfaceContext context,
                             WriteModeEnum writeOrderMode,
                             String requestNo,
                             OrderStatusEnum expectOrderStatus,
                             OrderStatusEnum orderStatus,
                             String responseNo);

    /**
     * 根据会话号查询请求订单物理主键
     *
     * @param sessionId         会话号
     * @param expectOrderStatus 期望订单状态
     * @return 请求订单物理主键
     */
    String queryRequestBySessionId(InterfaceContext context,
                                   String sessionId,
                                   OrderStatusEnum expectOrderStatus);
}
