package com.u002.mantis.client;

import com.u002.mantis.ProtostuffSerialization;
import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import com.u002.mantis.Serializer;
import com.u002.mantis.transport.codec.RpcDecoder;
import com.u002.mantis.transport.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 客户端初始化
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        Serializer serializer=new ProtostuffSerialization();
        cp.addLast(new RpcEncoder(RpcRequest.class, serializer));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new RpcDecoder(RpcResponse.class, serializer));
        cp.addLast(new RpcClientHandler());
    }
}
