package com.u002.basic.client;

import com.u002.basic.Connection;

import java.net.InetSocketAddress;

public interface RemoterClient {

    /**
     * 创建连接
     *
     * @param serverAddress 地址
     * @param listener 监听器
     */
    void connect(InetSocketAddress serverAddress, Listener listener);


    void stop();


    interface Listener {
        void onSuccess(Connection clientHandler);

        void onClose();
    }
}
