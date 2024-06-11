package com.u002.mantis;

import com.u002.mantis.proxy.IAsyncObjectProxy;

/**
 * 客户端接口
 */
public interface Client {

    /**
     * 远程接口调用的代理
     * @param interfaceClass 接口类
     * @return 代理对象
     * @param <T> 类型
     */
    public <T> T create(Class<T> interfaceClass);

    /**
     * 异步远程接口调用的代理
     * @param interfaceClass 接口类
     * @return 代理对象
     * @param <T> 类型
     */
    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass);

    /**
     * 停止 生命周期维护
     */
    public void stop();
}
