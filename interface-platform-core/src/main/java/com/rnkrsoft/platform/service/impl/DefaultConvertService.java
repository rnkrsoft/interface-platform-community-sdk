package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.service.ConvertService;

/**
 * Created by rnkrsoft.com on 2018/6/24.
 */
public class DefaultConvertService implements ConvertService{
    @Override
    public boolean code(InterfaceContext ctx) {
        return true;
    }

    @Override
    public boolean decode(InterfaceContext ctx) {
        return true;
    }
}
