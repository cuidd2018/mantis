package com.u002.mantis.remote;

import com.u002.mantis.RpcRequest;
import com.u002.mantis.task.RpcFuture;

public interface Transfer {
    RpcFuture call(RpcRequest request);
}
