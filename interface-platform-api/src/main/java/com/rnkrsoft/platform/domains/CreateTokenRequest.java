package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.annotation.ApidocElement;
import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTokenRequest implements Serializable{
    @ApidocElement("用户号")
    String userId;
    @ApidocElement("用户名")
    String userName;
    @ApidocElement("TOKEN过期秒数")
    Integer expireSecond;
    @ApidocElement(value = "通道")
    String channel;
}
