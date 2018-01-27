package com.rnkrsoft.platform.service;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;
import com.rnkrsoft.platform.InterfaceResult;
import com.rnkrsoft.platform.enums.OrderStatusEnum;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rnkrsoft.com on 2018/6/27.
 * 抽象信息服务，对同步和异步方式进行代理
 */
@Slf4j
public abstract class AbstractMessageService implements MessageService {
    //异步写入标记
    @Setter
    boolean asyncWrite = false;
    @Setter
    int asyncExecuteThreadPoolSize = 10;

    ExecutorService asyncWriteThreadPool;

    @Override
    public void init() {
        if (!this.asyncWrite) {
            return;
        }
        if (this.asyncWriteThreadPool != null) {
            if (!this.asyncWriteThreadPool.isShutdown() || !this.asyncWriteThreadPool.isTerminated()) {
                this.asyncWriteThreadPool.shutdown();
                this.asyncWriteThreadPool = null;
            }
        }
        this.asyncWriteThreadPool = Executors.newFixedThreadPool(asyncExecuteThreadPoolSize);
    }

    /**
     * 提交异步写入请求
     *
     * @param context     上下文
     * @param requestNo   请求订单号
     * @param innerData   内部数据
     * @param outerData   外部数据
     * @param orderStatus 订单状态
     * @return 句柄
     */
    protected Future<Boolean> submitAsyncWriteRequest(final InterfaceContext context,
                                                      final String requestNo,
                                                      final String responseNo,
                                                      final InterfaceData innerData,
                                                      final InterfaceData outerData,
                                                      final OrderStatusEnum orderStatus,
                                                      final Date createDate,
                                                      final Date lastUpdateDate) {
        if (this.asyncWriteThreadPool == null) {
            throw ErrorContextFactory.instance().activity("提交异步写入请求")
                    .message("异步写入线程池未调用init方法")
                    .runtimeException();
        }
        if (log.isDebugEnabled()) {
            log.debug("异步写入请求信息提交成功");
        }
        return this.asyncWriteThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    createRequestSync(context, requestNo, responseNo, innerData, outerData, orderStatus, createDate, lastUpdateDate);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * 提交异步写入应答
     *
     * @param context         上下文
     * @param responseNo      应答订单号
     * @param requestNo       请求订单号
     * @param rspType         应答类型
     * @param innerData       内部数据
     * @param outerData       外部数据
     * @param innerResult     内部结果
     * @param outerResult     外部结果
     * @param causeStackTrace 异常栈
     * @param causeMessage    异常信息
     * @return 句柄
     */
    protected Future<Boolean> submitAsyncWriteResponse(final InterfaceContext context,
                                                       final String responseNo,
                                                       final String requestNo,
                                                       final RspTypeEnum rspType,
                                                       final InterfaceData innerData,
                                                       final InterfaceData outerData,
                                                       final InterfaceResult innerResult,
                                                       final InterfaceResult outerResult,
                                                       final String causeStackTrace,
                                                       final String causeMessage,
                                                       final Date createDate,
                                                       final Date lastUpdateDate) {
        if (this.asyncWriteThreadPool == null) {
            throw ErrorContextFactory.instance().activity("提交异步写入请求")
                    .message("异步写入线程池未调用init方法")
                    .runtimeException();
        }
        if (log.isDebugEnabled()) {
            log.debug("异步写入应答信息提交成功");
        }
        return this.asyncWriteThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    createResponseSync(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, causeStackTrace, causeMessage, createDate, lastUpdateDate);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * 创建请求订单数据
     *
     * @param context
     * @param requestNo   请求订单号
     * @param innerData   系统内侧报文
     * @param outerData   系统外侧报文
     * @param orderStatus 订单状态         @return 请求订单号
     */
    public abstract String createRequestSync(InterfaceContext context,
                                             String requestNo,
                                             String response,
                                             InterfaceData innerData,
                                             InterfaceData outerData,
                                             OrderStatusEnum orderStatus,
                                             Date createDate,
                                             Date lastUpdateDate);

    public String createRequestAsync(InterfaceContext context,
                                     String requestNo,
                                     String responseNo,
                                     InterfaceData innerData,
                                     InterfaceData outerData,
                                     OrderStatusEnum orderStatus,
                                     Date createDate,
                                     Date lastUpdateDate) {
        submitAsyncWriteRequest(context, requestNo, responseNo, innerData, outerData, orderStatus, createDate, lastUpdateDate);
        return context.getRequestNo();
    }

    /**
     * 创建应答订单
     *
     * @param context
     * @param responseNo  应答订单号
     * @param requestNo   请求订单号
     * @param rspType     应答类型
     * @param innerData   系统内侧报文
     * @param outerData   系统外侧报文
     * @param innerResult 系统内侧结果
     * @param outerResult 系统外侧结果
     * @return 应答订单号
     */
    public abstract String createResponseSync(InterfaceContext context,
                                              String responseNo,
                                              String requestNo,
                                              RspTypeEnum rspType,
                                              InterfaceData innerData,
                                              InterfaceData outerData,
                                              InterfaceResult innerResult,
                                              InterfaceResult outerResult,
                                              Date createDate,
                                              Date lastUpdateDate);


    public String createResponseAsync(InterfaceContext context,
                                      String responseNo,
                                      String requestNo,
                                      RspTypeEnum rspType,
                                      InterfaceData innerData,
                                      InterfaceData outerData,
                                      InterfaceResult innerResult,
                                      InterfaceResult outerResult,
                                      Date createDate,
                                      Date lastUpdateDate) {
        submitAsyncWriteResponse(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, null, null, createDate,lastUpdateDate);
        return context.getResponseNo();
    }

    /**
     * 创建应答订单
     *
     * @param context
     * @param responseNo      应答订单号
     * @param requestNo       请求订单号
     * @param rspType         应答类型
     * @param innerData       系统内侧报文
     * @param outerData       系统外侧报文
     * @param innerResult     系统内侧结果
     * @param outerResult     系统外侧结果
     * @param causeStackTrace 发生异常堆栈
     * @param causeMessage    异常信息
     * @return 应答订单号
     */
    public abstract String createResponseSync(InterfaceContext context,
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

    public String createResponseAsync(InterfaceContext context,
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
                                      Date lastUpdateDate) {
        submitAsyncWriteResponse(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, causeStackTrace, causeMessage, createDate, lastUpdateDate);
        return context.getResponseNo();
    }

    @Override
    public String createRequest(InterfaceContext context,
                                WriteModeEnum writeOrderMode,
                                String requestNo,
                                String responseNo,
                                InterfaceData innerData,
                                InterfaceData outerData,
                                OrderStatusEnum orderStatus,
                                Date createDate,
                                Date lastUpdateDate) {
        if (writeOrderMode == WriteModeEnum.SYNC) {
            return createRequestSync(context, requestNo, responseNo, innerData, outerData, orderStatus, createDate, lastUpdateDate);
        } else {
            return createRequestAsync(context, requestNo, responseNo, innerData, outerData, orderStatus, createDate, lastUpdateDate);
        }
    }

    @Override
    public String createResponse(InterfaceContext context,
                                 WriteModeEnum writeOrderMode,
                                 String responseNo,
                                 String requestNo,
                                 RspTypeEnum rspType,
                                 InterfaceData innerData,
                                 InterfaceData outerData,
                                 InterfaceResult innerResult,
                                 InterfaceResult outerResult,
                                 Date createDate,
                                 Date lastUpdateDate) {
        if (writeOrderMode == WriteModeEnum.SYNC) {
            return createResponseSync(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, createDate, lastUpdateDate);
        } else {
            return createResponseAsync(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, createDate, lastUpdateDate);
        }
    }

    @Override
    public String createResponse(InterfaceContext context,
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
                                 Date lastUpdateDate) {
        if (writeOrderMode == WriteModeEnum.SYNC) {
            return createResponseSync(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, causeStackTrace, causeMessage, createDate, lastUpdateDate);
        } else {
            return createResponseAsync(context, responseNo, requestNo, rspType, innerData, outerData, innerResult, outerResult, causeStackTrace, causeMessage, createDate, lastUpdateDate);
        }
    }
}
