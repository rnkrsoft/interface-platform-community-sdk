package com.rnkrsoft.platform.mongo.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.jdbc.Comment;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/6/26.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MongoTable(name = "INTERFACE_REQUEST")
public class MongoInterfaceRequestEntity implements Serializable{
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @MongoColumn(name = "_id", nullable = false, valueMode = ValueMode.EQUAL)
    String requestNo;

    @MongoColumn(name = "SESSION_ID", nullable = true, valueMode = ValueMode.EQUAL)
    String sessionId;

    @MongoColumn(name = "CHANNEL", nullable = true)
    String channel;

    @MongoColumn(name = "TX_NO", nullable = true)
    String txNo;

    @MongoColumn(name = "VERSION", nullable = true)
    String version;

    @Comment("设备厂商 例如,xiaomi,apple")
    @MongoColumn(name = "DEVICE_MANUFACTURER", nullable = true)
    String deviceManufacturer;

    @Comment("设备型号 例如 xiaomi note, iphone 6s")
    @MongoColumn(name = "DEVICE_MODEL", nullable = true)
    String deviceModel;

    @Comment("MAC地址 例如 44-45-53-54-00-00")
    @MongoColumn(name = "MAC_ADDRESS", nullable = true)
    String macAddress;

    @Comment("设备类型 例如 iOS,Android,H5")
    @MongoColumn(name = "DEVICE_TYPE", nullable = true)
    String deviceType;


    @Comment("操作系统版本 例如 11.4.1")
    @MongoColumn(name = "OS_VERSION", nullable = true)
    String osVersion;

    @Comment("应用版本 例如 3.1.2")
    @MongoColumn(name = "APP_VERSION", nullable = true)
    String appVersion;

    @MongoColumn(name = "UIC", nullable = true)
    String uic;

    @MongoColumn(name = "UID", nullable = true)
    String uid;

    @MongoColumn(name = "LNG", nullable = true)
    BigDecimal lng;

    @MongoColumn(name = "LAT", nullable = true)
    BigDecimal lat;

    @MongoColumn(name = "CLIENT_IP", nullable = true)
    String clientIp;

    @MongoColumn(name = "CLIENT_PORT", nullable = true)
    Integer clientPort;

    @MongoColumn(name = "INNER_MESSAGE", nullable = true)
    String innerMessage;

    @MongoColumn(name = "OUTER_MESSAGE", nullable = true)
    String outerMessage;

    @MongoColumn(name = "ORDER_STATUS", nullable = false, valueMode = ValueMode.EQUAL)
    Integer orderStatus;

    @MongoColumn(name = "RESPONSE_NO", nullable = true)
    String responseNo;

    @MongoColumn(name = "CREATE_DATE", nullable = false)
    Date createDate;

    @MongoColumn(name = "LAST_UPDATE_DATE", nullable = false)
    Date lastUpdateDate;
}
