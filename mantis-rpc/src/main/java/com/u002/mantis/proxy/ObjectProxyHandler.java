package com.u002.mantis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ObjectProxyHandler<T> implements InvocationHandler {

    private final IAsyncObjectProxy remoteCall;

    public ObjectProxyHandler(IAsyncObjectProxy remoteCall) {
        this.remoteCall = remoteCall;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            switch (name) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);

                case "toString":
                    return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler " + this;
                default:
                    throw new IllegalStateException(String.valueOf(method));
            }

        }
        return remoteCall.call(method, args);
    }
}
