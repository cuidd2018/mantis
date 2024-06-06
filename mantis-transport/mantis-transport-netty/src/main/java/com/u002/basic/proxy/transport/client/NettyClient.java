package com.u002.basic.proxy.transport.client;


import com.u002.basic.Connection;
import com.u002.basic.client.RemoterClient;
import com.u002.basic.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NettyClient implements RemoterClient {

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private final Serializer serializer;

    private final ChannelHandler channelHandler;


    public NettyClient(Serializer serializer, ChannelHandler channelHandler) {
        this.serializer = serializer;
        this.channelHandler = channelHandler;
    }

    @Override
    public void connect(InetSocketAddress serverAddress, Listener listener) {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new RpcClientInitializer(serializer, channelHandler));
        connect(b, serverAddress, listener);
    }

    @Override
    public void stop() {
        eventLoopGroup.shutdownGracefully();
    }


    private void connect(final Bootstrap b, final InetSocketAddress remotePeer, Listener listener) {
        final ChannelFuture connectFuture = b.connect(remotePeer);
        connectFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                LOGGER.info("connectFuture.channel close operationComplete. remote peer = " + remotePeer);
                future.channel().eventLoop().schedule(new Runnable() {
                    public void run() {
                        LOGGER.warn("Attempting to reconnect.");
                        listener.onClose();
                        connect(b, remotePeer, listener);
                    }
                }, 3, TimeUnit.SECONDS);
            }
        });
        connectFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    LOGGER.info("Successfully connect to remote server. remote peer = " + remotePeer);
                    listener.onSuccess((Connection) channelHandler);
                } else {
                    LOGGER.error("Failed to connect.", future.cause());
                }
            }
        });
    }
}
