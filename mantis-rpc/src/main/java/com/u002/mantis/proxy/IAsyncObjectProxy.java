package com.u002.mantis.proxy;

import com.u002.mantis.task.RpcFuture;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface IAsyncObjectProxy {

    RpcFuture call(String funcName, Object... args);

    Object call(Method method, Object[] args)throws ExecutionException, InterruptedException, TimeoutException;

}