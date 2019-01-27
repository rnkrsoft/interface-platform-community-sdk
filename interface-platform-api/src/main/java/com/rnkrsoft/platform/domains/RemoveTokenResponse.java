package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.AbstractResponse;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
public class RemoveTokenResponse extends AbstractResponse{
}
