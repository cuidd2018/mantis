package com.u002.mantis.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * @author amber
 * @date 2017-08-09 09:16:20
 * @description 代理接口
 */
public interface IProxy {

    /**
     * 创建代理对象
     * @param clz 类
     * @param invocationHandler 处理器
     * @param classLoader 类加载器
     * @param <T> 类型参数
     * @return 代理类
     */
    public <T> T createInstance(Class<T> clz, InvocationHandler invocationHandler, ClassLoader classLoader);

    /**
     * 创建代理对象 当前线程的类加载器
     * @param clz 类
     * @param invocationHandler 处理器
     * @param <T> 类型参数
     * @return 代理类
     */
    default public <T> T createInstance(Class<T> clz, InvocationHandler invocationHandler) {
        return createInstance(clz, invocationHandler, Thread.currentThread().getContextClassLoader());
    }

}