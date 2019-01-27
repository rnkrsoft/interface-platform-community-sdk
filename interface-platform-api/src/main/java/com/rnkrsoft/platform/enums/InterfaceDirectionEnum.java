package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 * 接口方向
 */
public enum InterfaceDirectionEnum implements EnumStringCode {
    INNER("INNER", "内部调用"),
    OUTER("OUTER", "外部调用");
    String code;
    String desc;
    InterfaceDirectionEnum(String code, String desc) {
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

    public static InterfaceDirectionEnum valueOfCode(String code){
        for (InterfaceDirectionEnum value : values()){
            if (value.code.equals(code)){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的方向 '{}'", code).runtimeException();
    }
}
