package com.rnkrsoft.platform.enums;

import com.rnkrsoft.interfaces.EnumIntegerCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 * 接口信息记录状态
 */
public enum OrderStatusEnum implements EnumIntegerCode {
    SENT(0, "已发送"),
    FAIL(1, "失败"),
    SUCCESS(2, "成功"),
    TIMEOUT(3, "超时"),
    TIMEOUT_REDO_SUCCESS(4, "重做成功");
    int code;
    String desc;
    OrderStatusEnum(int code, String desc) {
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

    public static OrderStatusEnum valueOfCode(int code){
        for (OrderStatusEnum value : values()){
            if (value.code == code){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的订单状态 '{}'", code).runtimeException();
    }
}
