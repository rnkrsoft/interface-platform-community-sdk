package com.rnkrsoft.platform.cache.service;

import com.rnkrsoft.platform.cache.dao.TokenDAO;
import com.rnkrsoft.platform.cache.entity.TokenEntity;
import com.rnkrsoft.platform.domains.*;
import com.rnkrsoft.platform.enums.BaseRspCodeEnum;
import com.rnkrsoft.platform.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/19.
 */
public class CacheTokenService implements TokenService {
    @Autowired
    TokenDAO tokenDAO;

    @Override
    public CreateTokenResponse createToken(CreateTokenRequest request) {
        CreateTokenResponse response = new CreateTokenResponse();
        String token = UUID.randomUUID().toString();
        TokenEntity tokenEntity = TokenEntity.builder().token(token).channel(request.getChannel()).userId(request.getUserId()).userName(request.getUserName()).build();
        tokenDAO.saveByToken(token, tokenEntity);
        tokenDAO.saveByUid(request.getUserId(), tokenEntity);
        if (request.getExpireSecond() != null && request.getExpireSecond() > 0) {
            tokenDAO.refreshByToken(token, request.getExpireSecond());
            tokenDAO.refreshByUid(request.getUserId(), request.getExpireSecond());
        }
        response.setToken(token);
        return response;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenResponse response = new RefreshTokenResponse();
        tokenDAO.refreshByToken(request.getToken(), request.getExpireSecond());
        tokenDAO.refreshByUid(request.getUserId(), request.getExpireSecond());
        return response;
    }

    @Override
    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {
        ValidateTokenResponse response = new ValidateTokenResponse();
        if (request.getToken() == null) {
            response.setRspCode(BaseRspCodeEnum.PARAM_IS_NULL);
            return response;
        }
        TokenEntity tokenEntity = tokenDAO.getByToken(request.getToken());
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
            tokenEntity = tokenDAO.getByToken(request.getToken());
        } else if (request.getUserId() != null) {
            tokenEntity = tokenDAO.getByUid(request.getUserId());
        }
        RemoveTokenResponse response = new RemoveTokenResponse();
        if (tokenEntity == null) {
            return response;
        }
        tokenDAO.removeByToken(tokenEntity.getToken());
        tokenDAO.removeByUid(tokenEntity.getUserId());
        return response;
    }
}
