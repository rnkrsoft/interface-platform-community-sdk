package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.annotation.ApidocElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/19.
 */
@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenRequest implements Serializable{
    @ApidocElement("TOKEN值")
    String token;
}
