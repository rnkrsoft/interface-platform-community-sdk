package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import com.rnkrsoft.platform.protocol.enums.EnvironmentEnum;
import lombok.*;

/**
 * rnkrsoft.com 框架自动生成!
 */
@Table(name = "CONFIGURE_ENVIRONMENT",prefix = "TB")
@Comment("配置环境信息")
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigureEnvironmentEntity extends BaseEntity{
    @PrimaryKey(strategy = PrimaryKeyStrategy.IDENTITY)
    @Comment("环境代码")
    @NumberColumn(name = "ENV_CODE", nullable = false)
    Integer envCode;

    @Comment("环境类型")
    @NumberColumn(name = "ENV_TYPE", nullable = false, enumClass = EnvironmentEnum.class, defaultValue = "5")
    Integer envType;

    @Comment("环境描述")
    @StringColumn(name = "ENV_DESC", nullable = false)
    String envDesc;

    @Comment("是否启用")
    @NumberColumn(name = "ENABLED", nullable = false, defaultValue = "1")
    Boolean enabled;
}
