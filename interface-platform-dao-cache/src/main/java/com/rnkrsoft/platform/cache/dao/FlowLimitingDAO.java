package com.rnkrsoft.platform.cache.dao;

import com.rnkrsoft.framework.orm.cache.Cache;
import com.rnkrsoft.framework.orm.cache.CacheInterface;
import com.rnkrsoft.framework.orm.cache.Expire;
import com.rnkrsoft.framework.orm.cache.Incr;

/**
 * Created by rnkrsoft.com on 2018/10/12.
 */
@Cache(index = 2)
public interface FlowLimitingDAO extends CacheInterface {
    @Incr
    Long incr(String key);

    @Expire
    void expire(String key, int second);
}
