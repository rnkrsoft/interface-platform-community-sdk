package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import lombok.*;

@Table(name = "CONFIGURE_USER_ROUTING",prefix = "TB")
@Comment("配置用户路由信息")
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigureUserRoutingEntity extends BaseEntity {
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @Comment("物理主键")
    @StringColumn(name = "SERIAL_NO", nullable = false)
    String serialNo;

    @Comment("备注信息")
    @StringColumn(name = "COMMENT", nullable = true)
    String comment;

    @Comment("用户识别码")
    @StringColumn(name = "UIC", nullable = true)
    String uic;

    @Comment("设备类型，设备类型+用户识别码 优先匹配 ")
    @StringColumn(name = "DEVICE_TYPE", nullable = false)
    String deviceType;

    @Comment("应用版本号， 如果在设备类型+用户识别码下，存在版本号就优先匹配")
    @StringColumn(name = "APP_VERSION", nullable = true)
    String appVersion;

    @Comment("密钥向量")
    @StringColumn(name = "KEY_VECTOR", nullable = true, defaultValue = "123456")
    String keyVector;

    @Comment("HTTP连接超时时间")
    @NumberColumn(name = "HTTP_CONNECT_TIMEOUT_SECOND", nullable = false, defaultValue = "10")
    Integer httpConnectTimeoutSecond;

    @Comment("HTTP读取时间")
    @NumberColumn(name = "HTTP_READ_TIMEOUT_SECOND", nullable = false, defaultValue = "10")
    Integer httpReadTimeoutSecond;

    @Comment("异步执行线程池大小")
    @NumberColumn(name = "ASYNC_EXECUTE_THREAD_POOL_SIZE", nullable = false, defaultValue = "2")
    Integer asyncExecuteThreadPoolSize;

    @Comment("是否为调试模式")
    @NumberColumn(name = "DEBUG", nullable = false, defaultValue = "1")
    Boolean debug;

    @Comment("是否自动定位")
    @NumberColumn(name = "AUTO_LOCATE", nullable = false, defaultValue = "0")
    Boolean autoLocate;

    @Comment("是否打印啰嗦日志")
    @NumberColumn(name = "VERBOSE_LOG", nullable = false, defaultValue = "1")
    Boolean verboseLog;

    @Comment("环境，关联配置环境表")
    @NumberColumn(name = "ENV", nullable = false)
    Integer env;

    @Comment("优先级")
    @NumberColumn(name = "PRIORITY", nullable = false, defaultValue = "1")
    Integer priority;

    @Comment("是否启用")
    @NumberColumn(name = "ENABLED", nullable = false, defaultValue = "1")
    Boolean enabled;
}
