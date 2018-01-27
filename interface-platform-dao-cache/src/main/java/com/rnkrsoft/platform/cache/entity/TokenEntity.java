package com.rnkrsoft.platform.cache.entity;

import lombok.*;

/**
 * Created by rnkrsoft.com on 2018/6/12.
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    /**
     * TOKEN值
     */
    String token;
    /**
     * 用户号
     */
    String userId;
    /**
     * 用户名
     */
    String userName;
    /**
     * 通道
     */
    String channel;
}
