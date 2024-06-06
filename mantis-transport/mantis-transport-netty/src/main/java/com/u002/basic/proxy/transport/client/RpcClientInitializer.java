package com.u002.basic.proxy.transport.client;


import com.u002.basic.codec.RpcDecoder;
import com.u002.basic.codec.RpcEncoder;
import com.u002.basic.serialize.Serializer;
import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 客户端初始化
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {


    private final Serializer serializer;
    private final ChannelHandler channelHandler;



    public RpcClientInitializer(Serializer serializer, ChannelHandler channelHandler) {
        this.serializer = serializer;
        this.channelHandler = channelHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new RpcEncoder(RpcRequest.class, serializer));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new RpcDecoder(RpcResponse.class, serializer));
        cp.addLast(channelHandler);
    }
}
