package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumStringCode;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 * 接口信息记录方式
 */
public enum WriteModeEnum implements EnumStringCode {
    SYNC("SYNC", "同步"),
    ASYNC("ASYNC", "异步");
    String code;
    String desc;
    WriteModeEnum(String code, String desc) {
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

    public static WriteModeEnum valueOfCode(String code){
        for (WriteModeEnum value : values()){
            if (value.code.equals(code)){
                return value;
            }
        }
        return SYNC;
    }
}
