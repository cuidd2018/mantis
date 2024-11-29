package com.u002.mantis.spring;

import com.u002.mantis.ConsumerBeanFactory;
import com.u002.mantis.config.api.ConsumerConfig;
import org.springframework.beans.factory.FactoryBean;

public class ConsumerFactoryBean<T> extends ConsumerConfig implements FactoryBean<T> {

    private transient T bean = null;
    private transient Class<?> objectType = null;

    private final ConsumerBeanFactory<T> consumerBeanFactory = null;

    @Override
    public T getObject() throws Exception {
        bean = consumerBeanFactory.refer();
        return bean;
    }

    @Override
    public Class<?> getObjectType() {
        try {
            objectType = consumerBeanFactory.getProxyClass();
        } catch (Exception e) {
            objectType = null;
        }
        return objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
