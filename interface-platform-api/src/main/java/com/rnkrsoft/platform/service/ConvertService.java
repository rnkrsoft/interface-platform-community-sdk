package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.InterfaceContext;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 */
public interface ConvertService {
    /**
     * 编码
     * @param ctx
     * @return
     */
    boolean code(InterfaceContext ctx);

    /**
     * 解码
     * @param ctx
     * @return
     */
    boolean decode(InterfaceContext ctx);
}
