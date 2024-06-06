package com.u002.mantis.remote;

import com.u002.mantis.RpcFuture;
import com.u002.mantis.RpcRequest;

public interface Transfer {
    RpcFuture call(RpcRequest request);
}
