package com.u002.basic.codec;

import com.u002.basic.MessageCodec;
import com.u002.basic.serialize.Serializer;
import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import io.netty.channel.ChannelHandler;

public class DefaultCodec implements MessageCodec {
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