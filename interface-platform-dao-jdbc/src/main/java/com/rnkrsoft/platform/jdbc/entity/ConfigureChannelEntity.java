package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import lombok.*;

/**
 * rnkrsoft.com 框架自动生成!
 */
@Table(name = "CONFIGURE_CHANNEL",prefix = "TB")
@Comment("配置通道信息")
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigureChannelEntity extends BaseEntity{
    @PrimaryKey(strategy = PrimaryKeyStrategy.AUTO)
    @Comment("通道号")
    @StringColumn(name = "CHANNEL", nullable = false)
    String channel;

    @Comment("通道描述")
    @StringColumn(name = "CHANNEL_DESC", nullable = false)
    String channelDesc;

    @Comment("是否启用")
    @NumberColumn(name = "ENABLED", nullable = false, defaultValue = "1")
    Boolean enabled;
}
