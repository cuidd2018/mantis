package com.u002.mantis.transport;

import com.u002.mantis.RpcRequest;
import com.u002.mantis.task.RpcFuture;

import java.net.SocketAddress;

public interface Connection {

    SocketAddress getRemoteAddress();

    void close();

    RpcFuture sendRequest(RpcRequest request);
}
