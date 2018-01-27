package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngine;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DataEngine(DataEngineType.MyISAM)
@Table(name = "INTERFACE_REQUEST", prefix = "TB", suffix = "")
@Comment("请求信息")
public class InterfaceRequestEntity extends BaseEntity  implements Serializable {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @Comment("请求订单号")
    @StringColumn(name = "REQUEST_NO", nullable = false, type = StringType.VARCHAR)
    String requestNo;

    @Comment("会话号，与日志保持一致")
    @StringColumn(name = "SESSION_ID", nullable = true, type = StringType.VARCHAR, length = 36)
    String sessionId;

    @Comment("通道")
    @StringColumn(name = "CHANNEL", nullable = false, type = StringType.VARCHAR)
    String channel;

    @Comment("交易码")
    @StringColumn(name = "TX_NO", nullable = false, type = StringType.VARCHAR, length = 36)
    String txNo;

    @Comment("版本号")
    @StringColumn(name = "VERSION", nullable = false, type = StringType.VARCHAR, length = 36)
    String version;

    @Comment("设备厂商 例如,xiaomi,apple")
    @StringColumn(name = "DEVICE_MANUFACTURER", nullable = true, type = StringType.VARCHAR)
    String deviceManufacturer;

    @Comment("设备型号 例如 xiaomi note, iphone 6s")
    @StringColumn(name = "DEVICE_MODEL", nullable = true, type = StringType.VARCHAR)
    String deviceModel;

    @Comment("MAC地址 例如 44-45-53-54-00-00")
    @StringColumn(name = "MAC_ADDRESS", nullable = true, type = StringType.VARCHAR, length = 36)
    String macAddress;

    @Comment("设备类型 例如 iOS,Android,H5")
    @StringColumn(name = "DEVICE_TYPE", nullable = true, type = StringType.VARCHAR)
    String deviceType;

    @Comment("操作系统版本 例如 11.4.1")
    @StringColumn(name = "OS_VERSION", nullable = true, type = StringType.VARCHAR)
    String osVersion;

    @Comment("应用版本 例如 3.1.2")
    @StringColumn(name = "APP_VERSION", nullable = true, type = StringType.VARCHAR)
    String appVersion;

    @Comment("用户识别码,用于标记用户设备 例如 388c4292-b78f-4f53-9dee-f44b123f05ba")
    @StringColumn(name = "UIC", nullable = true, type = StringType.VARCHAR)
    String uic;

    @Comment("用户号,用户在登录的情况下捕捉，可能为电话号码，1850000000")
    @StringColumn(name = "UID", nullable = true, type = StringType.VARCHAR)
    String uid;

    @Comment("客户端调用的经度")
    @NumberColumn(name = "lng", nullable = true, type = NumberType.DECIMAL, precision = 15, scale = 10)
    BigDecimal lng;

    @Comment("客户端调用的纬度")
    @NumberColumn(name = "lat", nullable = true, type = NumberType.DECIMAL, precision = 15, scale = 10)
    BigDecimal lat;

    @Comment("客户端IP地址")
    @StringColumn(name = "CLIENT_IP", nullable = true, type = StringType.VARCHAR)
    String clientIp;

    @Comment("客户端端口号")
    @NumberColumn(name = "CLIENT_PORT", nullable = true, type = NumberType.INTEGER)
    Integer clientPort;

    @Comment("系统内侧信息")
    @StringColumn(name = "INNER_MESSAGE", nullable = true, type = StringType.TEXT)
    String innerMessage;

    @Comment("系统外侧信息")
    @StringColumn(name = "OUTER_MESSAGE", nullable = true, type = StringType.TEXT)
    String outerMessage;

    @Comment("订单状态")
    @NumberColumn(name = "ORDER_STATUS", nullable = false, type = NumberType.INTEGER)
    Integer orderStatus;

    @Comment("处理该请求到终态的应答号订单信息")
    @StringColumn(name = "RESPONSE_NO", nullable = true, type = StringType.VARCHAR)
    String responseNo;
}
