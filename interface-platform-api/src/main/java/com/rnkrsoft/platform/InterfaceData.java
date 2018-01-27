package com.rnkrsoft.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/6/25.
 * 接口数据包装对象
 */
@ToString
@Builder
public final class InterfaceData {
    /**
     * 明文
     */
    @Setter
    String plainText;
    /**
     * 密文
     */
    @Setter
    @Getter
    String cipherText;
    /**
     * 参数签名
     */
    @Setter
    @Getter
    String sign;
    /**
     * 参数对象类型
     */
    @Setter
    @Getter
    Class<?> objectClass;
    /**
     * 对象
     */
    Object object;

    private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().setDateFormat("yyyyMMddHHmmss").create();

    public InterfaceData() {
    }

    public InterfaceData(String plainText, String cipherText, String sign, Class<?> objectClass, Object object) {
        this.plainText = plainText;
        this.cipherText = cipherText;
        this.sign = sign;
        this.objectClass = objectClass;
        this.object = object;
    }

    public void setObject(Object object) {
        this.object = object;
        if (object != null) {
            this.objectClass = object.getClass();
        }
    }

    public <T> T getObject() {
        if (object == null){
            this.object = (T) GSON.fromJson(plainText, objectClass);
        }
        return (T) object;
    }

    public String getPlainText() {
        if (plainText == null) {
            this.plainText = GSON.toJson(object);
        }
        return plainText;
    }

    public boolean isReady4Encrypt() {
        return (plainText != null && !plainText.isEmpty()) || object != null;
    }

    public boolean isReady4Decrypt() {
        return (cipherText != null && !cipherText.isEmpty()) || object != null;
    }

}
