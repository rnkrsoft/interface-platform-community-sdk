package com.rnkrsoft.platform.definition.file;

import com.rnkrsoft.com.google.gson.Gson;
import com.rnkrsoft.com.google.gson.GsonBuilder;
import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.enums.InterfaceTypeEnum;
import com.rnkrsoft.platform.enums.WriteModeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2019/1/26.
 */
@Getter
@Setter
@Builder
public class InterfaceDefinitionMetadata implements Serializable{
    String channel;
    String txNo;
    String version;
    String interfaceDesc;
    String interfaceType = InterfaceTypeEnum.SYNC.getCode();
    String interfaceDirection = InterfaceDirectionEnum.INNER.getCode();
    String gatewayUrl;
    int httpTimeoutSecond;
    String serviceClassName;
    String methodName;
    String encryptAlgorithm;
    String decryptAlgorithm;
    String signAlgorithm;
    String verifyAlgorithm;
    boolean useTokenAsPassword;
    boolean firstSignSecondEncrypt;
    boolean firstVerifySecondDecrypt;
    boolean validateToken;
    boolean idempotentRedo;
    boolean writeMessage;
    String writeMode = WriteModeEnum.SYNC.getCode();
    boolean enabled;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceDefinitionMetadata that = (InterfaceDefinitionMetadata) o;

        if (!channel.equals(that.channel)) return false;
        if (!txNo.equals(that.txNo)) return false;
        return version.equals(that.version);

    }

    @Override
    public int hashCode() {
        int result = channel.hashCode();
        result = 31 * result + txNo.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }
}
