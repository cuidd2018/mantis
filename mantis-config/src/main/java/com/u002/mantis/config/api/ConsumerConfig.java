package com.u002.mantis.config.api;

import com.u002.mantis.Client;
import com.u002.mantis.ConsumerBeanFactory;
import org.apache.commons.lang3.StringUtils;

public class ConsumerConfig<T> extends AbstractInterfaceConfig implements ConsumerBeanFactory<T> {

    /**
     * 直连调用地址
     */
    protected String url;

    /**
     * 连接超时时间
     */
    protected int connectTimeout;

    /**
     * 代理类
     */
    private volatile transient T proxyIns = null;

    public T refer() {
        //返回代理类
        if (proxyIns != null)
            return proxyIns;
        try {
            proxyIns = (T) getProxyClass().newInstance();
            initConnections();
        } catch (Exception e) {
            throw new RuntimeException("Build consumer proxy error!", e);
        }
        return proxyIns;
    }

    /**
     * 初始化连接没有实现。
     */
    private void initConnections() {
        Client client = (Client) proxyIns;
    }

    public Class<?> getProxyClass() {
        if (proxyClass != null) {
            return proxyClass;
        }
        try {
            if (StringUtils.isNotBlank(interfaceClass)) {
                this.proxyClass = Class.forName(interfaceClass);
            } else {
                throw new Exception("consumer.interfaceId, null, interfaceId must be not null");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return proxyClass;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets connect timeout.
     *
     * @return the connect timeout
     */
    public int getTimeout() {
        return connectTimeout;
    }

    /**
     * Sets connect timeout.
     *
     * @param connectTimeout the connect timeout
     */
    public void setTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
