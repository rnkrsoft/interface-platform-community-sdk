package com.rnkrsoft.platform.domains;

import lombok.*;

import javax.web.doc.AbstractResponse;

/**
 * Created by Administrator on 2018/6/19.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
public class RefreshTokenResponse extends AbstractResponse{
}
