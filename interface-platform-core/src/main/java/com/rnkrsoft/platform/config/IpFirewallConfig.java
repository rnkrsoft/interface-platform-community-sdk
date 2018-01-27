package com.rnkrsoft.platform.config;

import com.rnkrsoft.framework.config.annotation.DynamicConfig;
import com.rnkrsoft.framework.config.annotation.DynamicParam;
import lombok.Data;

/**
 * Created by rnkrsoft.com on 2018/12/2.
 */
@Data
@DynamicConfig
public class IpFirewallConfig {
    @DynamicParam(value = "${com.rnkrsoft.platform.ipfirewall.allowProvinces}", desc = "允许访问的省名称，多个用分号分割")
    String allowProvinces;

    @DynamicParam(value = "${com.rnkrsoft.platform.ipfirewall.strictCheckIp}", desc = "是否严格检查IP")
    Boolean strictCheckIp;

    @DynamicParam(value = "${com.rnkrsoft.platform.ipfirewall.maxThresholdPreMin}", desc = "每分钟阈值")
    int maxThresholdPreMin;

}
