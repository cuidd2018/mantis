package com.u002.mantis.config.api;

import com.u002.mantis.transport.Server;
import com.u002.mantis.transport.codec.DefaultCodec;
import com.u002.mantis.transport.internal.RpcServer;
import com.u002.serialize.protostuff.ProtostuffSerialization;

public class ServerConfig extends AbstractInterfaceConfig {

    /**
     * 绑定的地址。是某个网卡，还是全部地址
     */
    private final String host = "127.0.0.1";

    /**
     * 监听端口
     */
    protected int port;

    /**
     * 服务端对象
     */
    private volatile transient Server server = null;

    public void start() throws Exception {
        if (server == null) {
            server = new RpcServer(host + ":" + port,new DefaultCodec(new ProtostuffSerialization()));
        }
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public Server getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
