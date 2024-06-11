package com.u002.mantis.config.api;

import com.u002.basic.Server;
import com.u002.basic.proxy.transport.RpcServer;
import com.u002.mantis.provider.Processor;
import com.u002.mantis.provider.internal.ServiceProcessor;
import com.u002.basic.codec.DefaultCodec;
import com.u002.mantis.transport.internal.DefaultRpcHandler;
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


    /**
     * 处理过程对象。
     */
    private Processor processor;

    public void start() throws Exception {
        if (server == null) {
            processor= new ServiceProcessor();
            server = new RpcServer(host + ":" + port,new DefaultCodec(new ProtostuffSerialization()),new DefaultRpcHandler(processor));
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

    public Processor getProcessor() {
        return processor;
    }
}
