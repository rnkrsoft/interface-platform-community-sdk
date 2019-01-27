package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 * 信息转换服务
 */
public interface ConvertService {
    /**
     * 编码
     * @param ctx 上下文
     * @return 是否转换成功
     */
    boolean code(InterfaceContext ctx);

    /**
     * 解码
     * @param ctx 上下文
     * @return 是否转换成功
     */
    boolean decode(InterfaceContext ctx);
}
