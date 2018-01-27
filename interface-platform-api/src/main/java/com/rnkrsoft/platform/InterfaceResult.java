package com.rnkrsoft.platform;

import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import lombok.Builder;
import lombok.Data;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 */
@Data
@Builder
public class InterfaceResult {
    String code;
    String desc;

    public InterfaceResult() {
        this.code = InterfaceRspCode.SUCCESS.getCode();
        this.desc = InterfaceRspCode.SUCCESS.getDesc();
    }

    public InterfaceResult(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public void setEnum(EnumStringCode enumStringCode) {
        this.code = enumStringCode.getCode();
        this.desc = enumStringCode.getDesc();
    }
}
