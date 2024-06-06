package com.u002.mantis.transport;

import com.u002.mantis.config.api.ProviderConfig;

/**
 * 服务端接口
 */
public interface Server {
    void registerProcessor(ProviderConfig providerConfig);
}
