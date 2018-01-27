package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.platform.domains.*;
import com.rnkrsoft.platform.enums.BaseRspCodeEnum;
import com.rnkrsoft.platform.service.TokenService;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by rnkrsoft.com on 2018/10/7.
 * 基于虚拟机内部的实现的TOKEN服务
 */
public class InjvmTokenService implements TokenService {
    @Builder
    @Getter
    static class TokenEntity{
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

        long lastUpdateTime = System.currentTimeMillis();
    }
    private final static ConcurrentMap<String , TokenEntity> TOKENS = new ConcurrentSkipListMap<String, TokenEntity>();
    @Override
    public CreateTokenResponse createToken(CreateTokenRequest request) {
        CreateTokenResponse response = new CreateTokenResponse();
        String token = UUID.randomUUID().toString();
        TokenEntity tokenEntity = TokenEntity.builder().token(token).channel(request.getChannel()).userId(request.getUserId()).userName(request.getUserName()).lastUpdateTime(System.currentTimeMillis()).build();
        TOKENS.put(token, tokenEntity);
        TOKENS.put(request.getUserId(), tokenEntity);
        response.setToken(token);
        return response;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenResponse response = new RefreshTokenResponse();
        TokenEntity tokenEntity = TOKENS.get(request.getToken());
        tokenEntity.lastUpdateTime = System.currentTimeMillis();
        return response;
    }

    @Override
    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {
        ValidateTokenResponse response = new ValidateTokenResponse();
        if (request.getToken() == null) {
            response.setRspCode(BaseRspCodeEnum.FAIL);
            return response;
        }
        TokenEntity tokenEntity = TOKENS.get(request.getToken());
        if (tokenEntity == null) {
            response.setRspCode(BaseRspCodeEnum.FAIL);
            return response;
        }
        response.setUserId(tokenEntity.getUserId());
        response.setUserName(tokenEntity.getUserName());
        response.setChannel(tokenEntity.getChannel());
        return response;
    }

    @Override
    public RemoveTokenResponse removeToken(RemoveTokenRequest request) {
        TokenEntity tokenEntity = null;
        if (request.getToken() != null) {
            tokenEntity = TOKENS.get(request.getToken());
        } else if (request.getUserId() != null) {
            tokenEntity = TOKENS.get(request.getUserId());
        }
        RemoveTokenResponse response = new RemoveTokenResponse();
        if (tokenEntity == null) {
            return response;
        }
        TOKENS.remove(tokenEntity.getToken());
        TOKENS.remove(tokenEntity.getUserId());
        return response;
    }
}
