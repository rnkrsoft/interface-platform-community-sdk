package com.rnkrsoft.platform.jdbc.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.*;
import lombok.*;

/**
 * rnkrsoft.com 框架自动生成!
 */
@Table(name = "CONFIGURE_RULE_AREA",prefix = "TB")
@Comment("配置区域规则表")
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigureRuleAreaEntity extends BaseEntity{
    @PrimaryKey(strategy = PrimaryKeyStrategy.AUTO)
    @Comment("区域代码")
    @StringColumn(name = "AREA_CODE", nullable = false)
    String areaCode;

    @Comment("区域描述")
    @StringColumn(name = "AREA_DESC", nullable = false)
    String areaDesc;

    @Comment("规则状态")
    @NumberColumn(name = "RULE_STATE", nullable = false)
    Integer ruleState;

    @Comment("是否启用")
    @NumberColumn(name = "ENABLED", nullable = false)
    Boolean enabled;
}
