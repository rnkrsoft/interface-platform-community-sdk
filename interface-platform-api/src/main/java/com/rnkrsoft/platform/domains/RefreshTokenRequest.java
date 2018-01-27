package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.annotation.ApidocElement;

/**
 * Created by Administrator on 2018/6/19.
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequest {
    @ApidocElement("TOKEN值")
    String token;
    @ApidocElement("TOKEN过期秒数")
    Integer expireSecond;
    @ApidocElement("用户号")
    String userId;
}
