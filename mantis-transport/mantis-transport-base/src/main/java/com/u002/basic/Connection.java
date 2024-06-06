package com.u002.basic;

import com.u002.mantis.RpcFuture;
import com.u002.mantis.RpcRequest;


import java.net.SocketAddress;

public interface Connection {

    SocketAddress getRemoteAddress();

    void close();

    RpcFuture sendRequest(RpcRequest request);
}
