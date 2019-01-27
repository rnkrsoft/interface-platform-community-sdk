package com.rnkrsoft.platform.domains;

import com.rnkrsoft.platform.protocol.ApiRequest;
import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2019/1/26.
 */
@ToString
@Data
public class Request extends ApiRequest{
    /**
     * 客户端访问IP
     */
    String clientIp;
    /**
     * 客户端端口
     */
    int clientPort;
}
