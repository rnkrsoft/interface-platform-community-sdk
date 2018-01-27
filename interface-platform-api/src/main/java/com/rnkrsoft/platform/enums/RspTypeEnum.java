package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumIntegerCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
public enum RspTypeEnum implements EnumIntegerCode {
    SYNC_RESPONSE(1, "同步应答"),
    ASYNC_RETURN(2, "异步返回"),
    ASYNC_RESPONSE(3, "异步应答");
    int code;
    String desc;
    RspTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static RspTypeEnum valueOfCode(int code){
        for (RspTypeEnum value : values()){
            if (value.code == code){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的应答类型 '{}'", code).runtimeException();
    }
}
