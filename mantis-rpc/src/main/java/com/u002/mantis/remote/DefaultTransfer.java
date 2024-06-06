package com.u002.mantis.remote;

import com.u002.mantis.RpcFuture;
import com.u002.mantis.RpcRequest;
import com.u002.mantis.exception.MantisException;
import com.u002.basic.Connection;
import org.apache.commons.lang3.StringUtils;

public class DefaultTransfer implements Transfer {

    private final String serverAddress;

    public DefaultTransfer(String serverAddress) {
        if (StringUtils.isEmpty(serverAddress)) {
            throw new MantisException("serverAddress mast be not null!!!");
        }
        this.serverAddress = serverAddress;
        connect();
    }


    private void connect() {
        ConnectManager.getInstance().connect(this.serverAddress);
    }

    @Override
    public RpcFuture call(RpcRequest request) {
        //选择个连接发送数据
        Connection connection = ConnectManager.getInstance().chooseHandler();
        System.out.println("send request to serverAddress: " + serverAddress+"with handler "+connection);
        return connection.sendRequest(request);
    }
}
