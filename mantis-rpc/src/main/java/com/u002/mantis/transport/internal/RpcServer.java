package com.u002.mantis.transport.internal;


import com.u002.mantis.MessageCodec;
import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import com.u002.mantis.Serializer;
import com.u002.mantis.config.api.ProviderConfig;
import com.u002.mantis.provider.internal.ServiceProcessor;
import com.u002.mantis.transport.Server;
import com.u002.mantis.transport.codec.RpcDecoder;
import com.u002.mantis.transport.codec.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RpcServer  implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private final String serverAddress;

    /**
     * 服务暴漏用的，不应该放在这个地方
     */
    public volatile Map<String, Object> handleMap = new HashMap<>();

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();


    /**
     * 编解码器
     */
    protected MessageCodec codec;

    public RpcServer(String serverAddress) throws Exception {
        this.serverAddress = serverAddress;
        this.startServer();
    }


    @Override
    public void registerProcessor(ProviderConfig providerConfig) {
        handleMap.put(providerConfig.getInterface(), providerConfig.getRef());
    }

    private void startServer() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                .addLast(codec.newDecoder())
                                .addLast(codec.newEncoder())
                                .addLast(new RpcHandler(new ServiceProcessor(handleMap)));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        String[] array = serverAddress.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);

        ChannelFuture future = bootstrap.bind(host, port).sync();
        ChannelFuture channelFuture = future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    LOGGER.info("Server have success bind to " + serverAddress);
                } else {
                    LOGGER.error("Server fail bind to " + serverAddress);
                    throw new Exception("Server start fail !", future.cause());
                }
            }
        });

        try {
            channelFuture.await(5000, TimeUnit.MILLISECONDS);
            if (channelFuture.isSuccess()) {
                LOGGER.info("start easy rpc server success.");
            }
        } catch (InterruptedException e) {
            LOGGER.error("start easy rpc occur InterruptedException!", e);
        }
    }

    public void destroy() throws Exception {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }


    private static class DefaultCodec implements MessageCodec {
        private final Serializer serializer;

        public DefaultCodec(Serializer serializer) {
            this.serializer = serializer;
        }

        @Override
        public ChannelHandler newEncoder() {
            return new RpcEncoder(RpcResponse.class, serializer);
        }

        @Override
        public ChannelHandler newDecoder() {
            return new RpcDecoder(RpcRequest.class, serializer);
        }
    }
}
