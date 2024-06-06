package com.u002.mantis.provider.internal;

import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import com.u002.mantis.config.api.ProviderConfig;
import com.u002.mantis.provider.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServiceProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProcessor.class);

    private final Map<String, Object> handlerMap=new HashMap<>();


    public ServiceProcessor() {
    }

    @Override
    public RpcResponse invoke(RpcRequest request) {
        LOGGER.debug("Receive request " + request.getRequestId());
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t.toString());
            LOGGER.error("RPC Server handle request error", t);
        }
        return response;
    }

    @Override
    public void registerProcessor(ProviderConfig providerConfig) {
        handlerMap.put(providerConfig.getInterface(), providerConfig.getRef());
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // JDK reflect
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);

        // Cglib reflect
        /**FastClass serviceFastClass = FastClass.create(serviceClass);
         FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
         return serviceFastMethod.invoke(serviceBean, parameters);**/
    }
}
