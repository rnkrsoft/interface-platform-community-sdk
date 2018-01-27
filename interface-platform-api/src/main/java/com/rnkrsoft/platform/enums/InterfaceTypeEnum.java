package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
public enum InterfaceTypeEnum implements EnumStringCode {
    SYNC("SYNC", "同步"),
    ASYNC("ASYNC", "异步");
    String code;
    String desc;
    InterfaceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static InterfaceTypeEnum valueOfCode(int code){
        for (InterfaceTypeEnum value : values()){
            if (value.code.equals(code)){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的接口类型 '{}'", code).runtimeException();
    }
}
