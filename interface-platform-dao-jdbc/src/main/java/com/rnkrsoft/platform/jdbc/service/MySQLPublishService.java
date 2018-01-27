package com.rnkrsoft.platform.jdbc.service;

import com.rnkrsoft.platform.jdbc.dao.InterfaceDefinitionDAO;
import com.rnkrsoft.platform.jdbc.entity.InterfaceDefinitionEntity;
import com.rnkrsoft.platform.protocol.AsyncHandler;
import com.rnkrsoft.platform.protocol.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by rnkrsoft.com on 2018/6/29.
 */
public class MySQLPublishService implements PublishService {
    @Autowired
    InterfaceDefinitionDAO interfaceDefinitionDAO;
    @Override
    public FetchPublishResponse fetchPublish(FetchPublishRequest request) {
        FetchPublishResponse response = new FetchPublishResponse();
        for (String channel : request.getChannels()) {
            List<InterfaceDefinitionEntity> definitionEntities = interfaceDefinitionDAO.selectAnd(InterfaceDefinitionEntity.builder().channel(channel).build());
            InterfaceChannel interfaceChannel = new InterfaceChannel();
            interfaceChannel.setChannel(channel);

            for (InterfaceDefinitionEntity entity : definitionEntities){
                InterfaceDefinition interfaceDefinition = InterfaceDefinition.builder()
                        .channel(channel)
                        .txNo(entity.getTxNo())
                        .version(entity.getVersion())
                        .decryptAlgorithm(entity.getEncryptAlgorithm())
                        .encryptAlgorithm(entity.getDecryptAlgorithm())
                        .verifyAlgorithm(entity.getSignAlgorithm())
                        .signAlgorithm(entity.getVerifyAlgorithm())
                        .firstSignSecondEncrypt(!entity.getFirstVerifySecondDecrypt())
                        .firstVerifySecondDecrypt(!entity.getFirstSignSecondEncrypt())
                        .useTokenAsPassword(entity.getUseTokenAsPassword())
                        .build();
                interfaceChannel.getInterfaces().add(interfaceDefinition);
            }
            response.getChannels().add(interfaceChannel);
        }
        return response;
    }

    @Override
    public Future fetchPublish(FetchPublishRequest request, AsyncHandler<FetchPublishResponse> asyncHandler) {
        return null;
    }
}
