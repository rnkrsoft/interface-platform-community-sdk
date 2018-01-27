package com.rnkrsoft.platform.cache.dao;

import com.rnkrsoft.framework.orm.cache.*;
import com.rnkrsoft.platform.cache.entity.TokenEntity;

/**
 * Created by rnkrsoft.com on 2018/6/12.
 */
@Cache
public interface TokenDAO extends CacheInterface{

    @GetSet
    TokenEntity saveByToken(String token, TokenEntity value);

    @GetSet
    TokenEntity saveByUid(String uid, TokenEntity value);

    @Get
    TokenEntity getByToken(String token);

    @Get
    TokenEntity getByUid(String uid);

    @Expire
    void refreshByUid(String uid, int second);

    @Expire
    void refreshByToken(String token, int second);

    @Remove
    void removeByUid(String uid);

    @Remove
    void removeByToken(String token);

}
