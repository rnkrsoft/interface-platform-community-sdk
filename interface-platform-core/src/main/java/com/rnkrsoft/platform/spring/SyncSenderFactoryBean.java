package com.rnkrsoft.platform.spring;

import com.rnkrsoft.platform.proxy.InterfaceSenderProxyFactory;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 */
public class SyncSenderFactoryBean<T> implements FactoryBean<T>{

    @Setter
    Class<T> syncSenderInterface;

    @Override
    public T getObject() throws Exception {
        InterfaceSenderProxyFactory<T> factory = new InterfaceSenderProxyFactory<T>(syncSenderInterface);
        return factory.newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return syncSenderInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
