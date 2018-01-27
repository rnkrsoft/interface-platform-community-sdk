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
     * @param context
     * @param data 数据
     * @return 通过返回真
     */
    boolean sign(InterfaceContext context, InterfaceData data);
    /**
     * 验证签字
     * @param data 数据
     * @return 通过返回真
     */
    boolean verify(InterfaceContext context, InterfaceData data);

    /**
     * 加密数据
     * @param data 数据
     * @return 加密成功返回真
     */
    boolean encrypt(InterfaceContext context, InterfaceData data);

    /**
     * 解密数据
     * @param data 数据
     * @return 解密成功返回真
     */
    boolean decrypt(InterfaceContext context, InterfaceData data);
}
