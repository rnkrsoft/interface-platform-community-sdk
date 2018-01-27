package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngine;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import com.rnkrsoft.platform.enums.RspTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/6/19.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DataEngine(DataEngineType.MyISAM)
@Table(name = "INTERFACE_RESPONSE", prefix = "TB", suffix = "")
@Comment("应答信息")
public class InterfaceResponseEntity extends BaseEntity implements Serializable {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @Comment("应答号")
    @StringColumn(name = "RESPONSE_NO", nullable = false, type = StringType.VARCHAR)
    String responseNo;

    @Comment("应答类型")
    @NumberColumn(name = "RSP_TYPE", nullable = false, type = NumberType.INTEGER, enumClass = RspTypeEnum.class)
    Integer rspType;

    @Comment("系统内侧信息")
    @StringColumn(name = "INNER_MESSAGE", nullable = true, type = StringType.TEXT)
    String innerMessage;

    @Comment("系统外侧信息")
    @StringColumn(name = "OUTER_MESSAGE", nullable = true, type = StringType.TEXT)
    String outerMessage;

    @Comment("系统内侧应答码")
    @StringColumn(name = "INNER_RSP_CODE", nullable = true, type = StringType.VARCHAR)
    String innerRspCode;

    @Comment("系统内侧应答描述")
    @StringColumn(name = "INNER_RSP_DESC", nullable = true, type = StringType.VARCHAR)
    String innerRspDesc;

    @Comment("系统外侧应答码")
    @StringColumn(name = "OUTER_RSP_CODE", nullable = true, type = StringType.VARCHAR)
    String outerRspCode;

    @Comment("系统外侧应答描述")
    @StringColumn(name = "OUTER_RSP_DESC", nullable = true, type = StringType.VARCHAR)
    String outerRspDesc;

    @Comment("导致异常堆栈")
    @StringColumn(name = "CAUSE_STACK_TRACE", nullable = true, type = StringType.TEXT)
    String causeStackTrace;

    @Comment("导致异常的信息")
    @StringColumn(name = "CAUSE_MESSAGE", nullable = true, type = StringType.TEXT)
    String causeMessage;

    @Comment("请求订单号，与请求记录保持一致")
    @StringColumn(name = "REQUEST_NO", nullable = false, type = StringType.VARCHAR)
    String requestNo;
}
