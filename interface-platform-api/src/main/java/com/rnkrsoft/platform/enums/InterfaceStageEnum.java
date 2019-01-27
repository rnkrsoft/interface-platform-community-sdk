package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 * 接口所处阶段
 */
public enum InterfaceStageEnum implements EnumStringCode {
    REQUEST("REQUEST", "请求"),
    RETURN("RETURN","返回"),
    RESPONSE("RESPONSE", "应答");
    String code;
    String desc;
    InterfaceStageEnum(String code, String desc) {
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

    public static InterfaceStageEnum valueOfCode(int code){
        for (InterfaceStageEnum value : values()){
            if (value.code.equals(code)){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的阶段 '{}'", code).runtimeException();
    }
}
