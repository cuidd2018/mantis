package com.u002.mantis.remote;

import com.u002.mantis.RpcRequest;
import com.u002.mantis.proxy.IAsyncObjectProxy;
import com.u002.mantis.task.RpcFuture;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RemoteCall<T> implements IAsyncObjectProxy {

    private long timeout;
    private Class<T> clazz;

    /**
     * TODO 改成单例的，从工厂里边取，自己极定义个工厂
     */
    private final Transfer transfer = new DefaultTransfer("127.0.0.1:18868");

    public RemoteCall(long timeout) {
        this.timeout = timeout;
    }

    public RemoteCall(Class<T> clazz, long timeout) {
        this.clazz = clazz;
    }

    @Override
    public Object call(Method method, Object[] args) throws ExecutionException, InterruptedException, TimeoutException {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        return transfer.call(request).get(timeout, TimeUnit.SECONDS);
    }


    @Override
    public RpcFuture call(String funcName, Object... args) {
        RpcRequest request = createRequest(this.clazz.getName(), funcName, args);
        return transfer.call(request);
    }


    private RpcRequest createRequest(String className, String methodName, Object[] args) {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(args);

        Class[] parameterTypes = new Class[args.length];
        // Get the right class type
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);

        return request;
    }

    private Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        switch (typeName) {
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
            default:
                return classType;
        }
    }
}