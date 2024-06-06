package com.u002.mantis.client;


import com.u002.basic.proxy.IProxy;
import com.u002.mantis.Client;
import com.u002.mantis.proxy.IAsyncObjectProxy;
import com.u002.mantis.proxy.ObjectProxyHandler;
import com.u002.mantis.remote.ConnectManager;
import com.u002.mantis.remote.RemoteCall;
import com.u002.proxy.internal.InternalProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClient implements Client {

    //代理类本地做了个缓存
    private final Map<Class, Object> proxyInstances = new ConcurrentHashMap<>();

    private final long timeout = 2000;

    private final IProxy proxy = new InternalProxy();


    @Override
    public <T> T create(Class<T> interfaceClass) {
        if (proxyInstances.containsKey(interfaceClass)) {
            return (T) proxyInstances.get(interfaceClass);
        } else {
            Object object = proxy.createInstance( interfaceClass, new ObjectProxyHandler<>(new RemoteCall<>(timeout)),Thread.currentThread().getContextClassLoader());
            proxyInstances.put(interfaceClass, object);
            return (T) object;
        }
    }

    @Override
    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new RemoteCall(interfaceClass, timeout);
    }

    public void stop() {
        ConnectManager.getInstance().stop();
    }

}