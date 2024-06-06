package com.u002.mantis.provider;

import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import com.u002.mantis.config.api.ProviderConfig;

/**
 *  远程调用过程处理文件。
 */
public interface Processor {
    RpcResponse invoke(RpcRequest request);

    void registerProcessor(ProviderConfig providerConfig);

}
