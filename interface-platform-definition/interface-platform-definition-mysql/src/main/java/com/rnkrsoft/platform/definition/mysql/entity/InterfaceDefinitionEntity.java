package com.rnkrsoft.platform.definition.mysql.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngine;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.enums.InterfaceTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * rnkrsoft.com 框架自动生成!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DataEngine(DataEngineType.MyISAM)
@Table(name = "INTERFACE_DEFINITION", prefix = "TB", suffix = "")
@Comment("接口定义信息")
public class InterfaceDefinitionEntity extends BaseEntity implements Serializable {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @Comment("物理主键 渠道号:交易码:版本号")
    @StringColumn(name = "SERIAL_NO", nullable = false, type = StringType.VARCHAR)
    String serialNo;

    @Comment("交易码")
    @StringColumn(name = "TX_NO", nullable = false, type = StringType.VARCHAR)
    String txNo;

    @Comment("版本号")
    @StringColumn(name = "VERSION", nullable = false, type = StringType.VARCHAR, defaultValue = "1")
    String version;

    @Comment("通道，默认为公共通道")
    @StringColumn(name = "CHANNEL", nullable = false, type = StringType.VARCHAR, defaultValue = "public")
    String channel;

    @Comment("接口类型")
    @StringColumn(name = "INTERFACE_TYPE", nullable = false, type = StringType.VARCHAR, enumClass = InterfaceTypeEnum.class, defaultValue = "SYNC")
    String interfaceType;

    @Comment("接口方向")
    @StringColumn(name = "INTERFACE_DIRECTION", nullable = false, type = StringType.VARCHAR, enumClass = InterfaceDirectionEnum.class, defaultValue = "INNER")
    String interfaceDirection;

    @Comment("网关地址")
    @StringColumn(name = "GATEWAY_URL", nullable = true, type = StringType.VARCHAR, enumClass = InterfaceDirectionEnum.class)
    String gatewayUrl;

    @Comment("HTTP同步通信时的超时时间（秒）")
    @NumberColumn(name = "HTTP_TIMEOUT_SECOND", nullable = false, type = NumberType.INTEGER, defaultValue = "0")
    Integer httpTimeoutSecond;

    @Comment("服务类名")
    @StringColumn(name = "SERVICE_CLASS_NAME", nullable = false, type = StringType.VARCHAR)
    String serviceClassName;

    @Comment("方法名")
    @StringColumn(name = "METHOD_NAME", nullable = false, type = StringType.VARCHAR)
    String methodName;

    @Comment("加密算法")
    @StringColumn(name = "ENCRYPT_ALGORITHM", nullable = false, defaultValue = "AES")
    String encryptAlgorithm;

    @Comment("解密算法")
    @StringColumn(name = "DECRYPT_ALGORITHM", nullable = false, defaultValue = "AES")
    String decryptAlgorithm;

    @Comment("签字算法")
    @StringColumn(name = "SIGN_ALGORITHM", nullable = false, defaultValue = "SHA512")
    String signAlgorithm;

    @Comment("验签算法")
    @StringColumn(name = "VERIFY_ALGORITHM", nullable = false, defaultValue = "SHA512")
    String verifyAlgorithm;

    @Comment("使用TOKEN作为密码")
    @NumberColumn(name = "USE_TOKEN_AS_PASSWORD", nullable = false, defaultValue = "1")
    Boolean useTokenAsPassword;

    @Comment("密码")
    @StringColumn(name = "PASSWORD", nullable = true)
    String password;

    @Comment("首先签字然后加密")
    @NumberColumn(name = "FIRST_SIGN_SECOND_ENCRYPT", nullable = false, defaultValue = "1")
    Boolean firstSignSecondEncrypt;

    @Comment("首先校验然后解密")
    @NumberColumn(name = "FIRST_VERIFY_SECOND_DECRYPT", nullable = false, defaultValue = "1")
    Boolean firstVerifySecondDecrypt;

    @Comment("接口描述")
    @StringColumn(name = "INTERFACE_DESC", nullable = true, type = StringType.TEXT)
    String interfaceDesc;

    @Comment("是否校验TOKEN")
    @NumberColumn(name = "VALIDATE_TOKEN", nullable = true, defaultValue = "1")
    Boolean validateToken;

    @Comment("是否允许幂等进入重做，只适用于异步接口")
    @NumberColumn(name = "IDEMPOTENT_REDO", nullable = true, defaultValue = "1")
    Boolean idempotentRedo;

    @Comment("是否记录通讯信息")
    @NumberColumn(name = "WRITE_MESSAGE", nullable = true, defaultValue = "1")
    Boolean writeMessage;

    @Comment("写入模式")
    @StringColumn(name = "WRITE_MODE", nullable = true, enumClass = WriteModeEnum.class, defaultValue = "SYNC")
    String writeMode;

    @Comment("秘钥向量")
    @StringColumn(name = "KEY_VECTOR", nullable = true, enumClass = WriteModeEnum.class, defaultValue = "1234567890654321")
    String keyVector;
}
