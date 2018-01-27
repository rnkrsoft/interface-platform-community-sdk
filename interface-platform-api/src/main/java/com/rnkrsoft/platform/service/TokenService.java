package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.domains.*;

import javax.web.doc.annotation.ApidocInterface;
import javax.web.doc.annotation.ApidocService;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
@ApidocService("TOKEN服务")
public interface TokenService {
    @ApidocInterface("创建TOKEN")
    CreateTokenResponse createToken(CreateTokenRequest request);

    @ApidocInterface("刷新TOKEN")
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    @ApidocInterface("校验TOKEN")
    ValidateTokenResponse validateToken(ValidateTokenRequest request);

    @ApidocInterface("移除TOKEN")
    RemoveTokenResponse removeToken(RemoveTokenRequest request);
}
