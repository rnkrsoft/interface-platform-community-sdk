package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.AbstractResponse;
import javax.web.doc.annotation.ApidocElement;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTokenResponse extends AbstractResponse{
    @ApidocElement("TOKEN值")
    String token;
}
