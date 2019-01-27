package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceData;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 * 安全服务
 */
public interface SecurityService {
    /**
     * 生成签字
     * @param context 上下文
     * @param data 数据
     * @return 成功返回真
     */
    boolean sign(InterfaceContext context, InterfaceData data);
    /**
     * 验证签字
     * @param context 上下文
     * @param data 数据
     * @return 成功返回真
     */
    boolean verify(InterfaceContext context, InterfaceData data);

    /**
     * 加密数据
     * @param context 上下文
     * @param data 数据
     * @return 成功返回真
     */
    boolean encrypt(InterfaceContext context, InterfaceData data);

    /**
     * 解密数据
     * @param context 上下文
     * @param data 数据
     * @return 成功返回真
     */
    boolean decrypt(InterfaceContext context, InterfaceData data);
}
