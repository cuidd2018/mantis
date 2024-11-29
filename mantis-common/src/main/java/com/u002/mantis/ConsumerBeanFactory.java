package com.u002.mantis;

public interface ConsumerBeanFactory<T> {

     T refer();

     Class<?> getProxyClass();
}
