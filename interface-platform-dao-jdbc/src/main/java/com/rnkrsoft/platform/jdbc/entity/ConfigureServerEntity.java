package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import lombok.*;

/**
 * rnkrsoft.com 框架自动生成!
 */
@Table(name = "CONFIGURE_SERVER",prefix = "TB")
@Comment("配置环境信息")
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigureServerEntity extends BaseEntity{
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @Comment("服务器唯一标识")
    @StringColumn(name = "SERVER_NO", nullable = false)
    String serverNo;

    @Comment("环境")
    @StringColumn(name = "ENV", nullable = false)
    String env;

    @Comment("渠道")
    @StringColumn(name = "CHANNEL", nullable = false)
    String channel;

    @Comment("服务器描述")
    @StringColumn(name = "SERVER_DESC")
    String serverDesc;

    @Comment("服务器地址")
    @StringColumn(name = "SERVER_ADDRESS", nullable = false, defaultValue = "localhost")
    String serverAddress;

    @Comment("服务器端口号")
    @NumberColumn(name = "SERVER_PORT", nullable = false, defaultValue = "80")
    Integer serverPort;

    @Comment("服务器上下文路径")
    @StringColumn(name = "SERVER_CONTEXT_PATH", nullable = false, defaultValue = "gateway")
    String serverContextPath;

    @Comment("是否为HTTPS")
    @NumberColumn(name = "HTTPS", nullable = false, defaultValue = "0")
    Boolean https;

    @Comment("优先级")
    @NumberColumn(name = "PRIORITY", nullable = false, defaultValue = "1")
    Integer priority;

    @Comment("是否启用")
    @NumberColumn(name = "ENABLED", nullable = false, defaultValue = "1")
    Boolean enabled;
}
