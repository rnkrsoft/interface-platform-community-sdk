package com.rnkrsoft.platform;

import com.rnkrsoft.platform.service.InterfaceService;

/**
 * Created by woate on 2019/1/27.
 */
public class InterfacePlatformHolder {
    static InterfaceService INSTANCE;
    public static void set(InterfaceService interfaceService){
        INSTANCE = interfaceService;
    }

    public static InterfaceService get(){
        return INSTANCE;
    }
}
