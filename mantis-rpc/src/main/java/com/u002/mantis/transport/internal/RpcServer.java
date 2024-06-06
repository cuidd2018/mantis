package com.u002.mantis.transport.internal;

import com.u002.mantis.MessageCodec;
import com.u002.mantis.transport.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class RpcServer  implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private final String serverAddress;

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 编解码器
     */
    protected final MessageCodec codec;

    private final ChannelHandler channelHandler;

    public RpcServer(String serverAddress,MessageCodec messageCodec,ChannelHandler channelHandler) throws Exception {
        this.serverAddress = serverAddress;
        this.codec=messageCodec;
        this.channelHandler=channelHandler;
        this.startServer();
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
                                .addLast(channelHandler);
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


}