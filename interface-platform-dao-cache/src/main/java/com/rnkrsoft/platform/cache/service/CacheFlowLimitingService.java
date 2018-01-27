package com.rnkrsoft.platform.cache.service;

import com.rnkrsoft.platform.cache.dao.FlowLimitingDAO;
import com.rnkrsoft.platform.service.FlowLimitingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rnkrsoft.com on 2018/10/12.
 */
@Slf4j
public class CacheFlowLimitingService implements FlowLimitingService {
    @Autowired
    FlowLimitingDAO flowLimitingDAO;

    @Override
    public boolean detects(String uid, String uic, String clientAddress, String channel, int maxThresholdPreMin) {
        String key = "ip:" + (System.currentTimeMillis() % (60* 1000)) + ":" + clientAddress + "@" + (channel == null ? "" : channel);
        int time = 2 * 60;
        long limit = flowLimitingDAO.incr(key);
        if (limit == 1) {
            flowLimitingDAO.expire(key, time);
        }
        if (log.isDebugEnabled()) {
            log.debug("limiting key : '{}'", key);
        }
        if (limit > maxThresholdPreMin) {
            log.error("uic:'{}', uid:'{}', address:'{}' 请求超过流量限制值, 当前值:'{}', 最大阈值:'{}/分' 对该ip进行惩罚，{}秒后恢复访问", uic, uid, clientAddress, limit, maxThresholdPreMin, time);
            return false;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("uic:'{}', uid:'{}', address:'{}' 请求未超过流量限制值, 当前值:'{}', 最大阈值:'{}/分'", uic, uid, clientAddress, limit, maxThresholdPreMin);
            }
            return true;
        }
    }
}
