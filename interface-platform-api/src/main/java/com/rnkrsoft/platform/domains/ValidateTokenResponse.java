package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.AbstractResponse;
import javax.web.doc.annotation.ApidocElement;

/**
 * Created by Administrator on 2018/6/19.
 */
@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenResponse extends AbstractResponse{
    @ApidocElement("用户号")
    String userId;
    @ApidocElement("用户名")
    String userName;
    @ApidocElement(value = "通道")
    String channel;
}
