package com.rnkrsoft.platform.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by rnkrsoft.com on 2018/7/25.
 */
public class InterfaceSenderProxyFactory<T> {
    Class<T> syncSenderInterface;

    public InterfaceSenderProxyFactory(Class<T> syncSenderInterface) {
        this.syncSenderInterface = syncSenderInterface;
    }

    public T newInstance(){
        InterfaceSenderProxy proxy = new InterfaceSenderProxy();
        T instance = (T) Proxy.newProxyInstance(syncSenderInterface.getClassLoader(), new Class[]{syncSenderInterface}, proxy);
        return instance;
    }
}
