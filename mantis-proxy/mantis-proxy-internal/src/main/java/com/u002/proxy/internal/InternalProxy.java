package com.u002.proxy.internal;

import com.u002.basic.proxy.IProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author			amber
 * @date 			2017-08-09 09:17:50
 * @description 	默认代理实现JDK
 */
public class InternalProxy implements IProxy {

	/**
	 *
	 * @param interfaces 类
	 * @param invocationHandler 处理器
	 * @param classLoader 类加载器
	 * @return 代理对象
	 * @param <T> 类型参数
	 */
	@Override
	public <T> T createInstance(Class<T> interfaces, InvocationHandler invocationHandler, ClassLoader classLoader) {
		return (T)Proxy.newProxyInstance(classLoader,new Class<?>[]{interfaces} , invocationHandler);
	}
	
}