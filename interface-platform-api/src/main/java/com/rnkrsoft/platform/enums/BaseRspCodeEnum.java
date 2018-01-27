package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumStringCode;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 基本的业务异常错误码
 */
public enum BaseRspCodeEnum implements EnumStringCode{
    SUCCESS("0000","成功"),
    PARAM_IS_NULL("0001","参数不能为空"),
    FAIL("9999","错误");
    String code;
    String desc;
    BaseRspCodeEnum(String code, String desc){
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

    public static BaseRspCodeEnum valueOfCode(String code){
        for (BaseRspCodeEnum value : values()){
            if(value.code.equals(code)){
                return value;
            }
        }
        return FAIL;
    }
}
