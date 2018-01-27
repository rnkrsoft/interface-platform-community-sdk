package com.rnkrsoft.platform.jdbc.service;

import com.rnkrsoft.platform.jdbc.dao.ConfigureChannelDAO;
import com.rnkrsoft.platform.jdbc.dao.ConfigureEnvironmentDAO;
import com.rnkrsoft.platform.jdbc.dao.ConfigureServerDAO;
import com.rnkrsoft.platform.jdbc.dao.ConfigureUserRoutingDAO;
import com.rnkrsoft.platform.jdbc.entity.ConfigureEnvironmentEntity;
import com.rnkrsoft.platform.jdbc.entity.ConfigureServerEntity;
import com.rnkrsoft.platform.jdbc.entity.ConfigureUserRoutingEntity;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.protocol.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by rnkrsoft.com on 2018/6/23.
 */
@Slf4j
public class MySQLConfigureServiceImpl implements ConfigureService {
    @Autowired
    ConfigureUserRoutingDAO configureUserRoutingDAO;
    @Autowired
    ConfigureChannelDAO configureChannelDAO;
    @Autowired
    ConfigureEnvironmentDAO configureEnvironmentDAO;
    @Autowired
    ConfigureServerDAO configureServerDAO;

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public FetchConfigureResponse fetchConfigure(FetchConfigureRequest request, String clientAddress, int clientPort) {
        FetchConfigureResponse.FetchConfigureResponseBuilder builder = FetchConfigureResponse.builder();
        String uic = request.getUic();
        String deviceType = request.getDeviceType();
        String appVersion = request.getAppVersion();
        //检测区号 和 IP是否一致 或 检测 经纬度
        ConfigureUserRoutingEntity configureUserRoutingEntity = null;

        //1.按照用户识别码进行匹配
        configureUserRoutingEntity = configureUserRoutingDAO.selectByUic(uic);
        if (configureUserRoutingEntity == null) {
            //2.按照设备类型,版本,且uic必须为空进行匹配
            configureUserRoutingEntity = configureUserRoutingDAO.selectByDeviceTypeAndAppVersion(deviceType, appVersion);
            if (configureUserRoutingEntity == null) {
                //3.按照版本,且uic,设备类型必须为空，并按照优先级进行降序排列
                configureUserRoutingEntity = configureUserRoutingDAO.selectByAppVersion(appVersion);
                if (configureUserRoutingEntity == null) {
                    FetchConfigureResponse response = new FetchConfigureResponse();
                    response.setCode(InterfaceRspCode.GATEWAY_CONFIGURE_IS_NOT_CONFIG);
                    return response;
                }
            }
        }
        ConfigureEnvironmentEntity configureEnvironmentEntity = configureEnvironmentDAO.selectByPrimaryKey(configureUserRoutingEntity.getEnv());
        if (!configureEnvironmentEntity.getEnabled()) {
            log.error("配置环境'{}:{}'已经禁用", configureEnvironmentEntity.getEnvCode(), configureEnvironmentEntity.getEnvDesc());
            FetchConfigureResponse response = new FetchConfigureResponse();
            response.setCode(InterfaceRspCode.ENVIRONMENT_IS_ALREADY_DISABLED);
            return response;
        }
        builder.env(configureEnvironmentEntity.getEnvType())
                .envDesc(configureEnvironmentEntity.getEnvDesc())
                .debug(configureUserRoutingEntity.getDebug())
                .httpConnectTimeoutSecond(configureUserRoutingEntity.getHttpConnectTimeoutSecond())
                .httpReadTimeoutSecond(configureUserRoutingEntity.getHttpReadTimeoutSecond())
                .verboseLog(configureUserRoutingEntity.getVerboseLog())
                .autoLocate(configureUserRoutingEntity.getAutoLocate())
                .keyVector(configureUserRoutingEntity.getKeyVector())
                .asyncExecuteThreadPoolSize(configureUserRoutingEntity.getAsyncExecuteThreadPoolSize());
        FetchConfigureResponse response = builder.build();
        Map<String, List<GatewayAddress>> gatewayAddresses = new HashMap();
        List<ConfigureServerEntity> configureServerEntities = configureServerDAO.selectServerByEnvChannels(configureUserRoutingEntity.getEnv(), request.getChannels());
        for (ConfigureServerEntity configureServerEntity : configureServerEntities) {
            String channel = configureServerEntity.getChannel();
            List<GatewayAddress> gatewayAddresses_ = gatewayAddresses.get(channel);
            if (gatewayAddresses_ == null) {
                gatewayAddresses_ = new ArrayList();
                gatewayAddresses.put(channel, gatewayAddresses_);
            }
            GatewayAddress gatewayAddress = new GatewayAddress(
                    //TODO 地区编码，用于分城市分服务器
                    "",
                    configureServerEntity.getHttps() ? "https" : "http",
                    configureServerEntity.getServerAddress(),
                    configureServerEntity.getServerPort(),
                    configureServerEntity.getServerContextPath()
            );
            gatewayAddresses_.add(gatewayAddress);
        }
        for (String channel : gatewayAddresses.keySet()) {
            GatewayChannel gatewayChannel = new GatewayChannel(channel);
            List<GatewayAddress> gatewayAddresses_ = gatewayAddresses.get(channel);
            gatewayChannel.getGatewayAddresses().addAll(gatewayAddresses_);
            response.getChannels().add(gatewayChannel);
        }
        response.setCode(InterfaceRspCode.SUCCESS);
        return response;
    }
}
