package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.annotation.ApidocElement;
import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveTokenRequest implements Serializable{
    @ApidocElement("TOKEN值")
    String token;
    @ApidocElement("用户号")
    String userId;
}
