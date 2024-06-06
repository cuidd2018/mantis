package com.u002.mantis.client;

import com.u002.basic.Connection;
import com.u002.mantis.RpcFuture;
import com.u002.mantis.RpcRequest;
import com.u002.mantis.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端默认处理过程。
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientHandler.class);

    private final Map<String, RpcFuture> pendingRpc = new ConcurrentHashMap<String, RpcFuture>();

    private volatile Channel channel;
    private SocketAddress remotePeer;

    @Override
    public SocketAddress getRemoteAddress() {
        return remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        LOGGER.debug("client read form server");
        String requestId = response.getRequestId();
        RpcFuture rpcFuture = pendingRpc.get(requestId);
        if (rpcFuture != null) {
            pendingRpc.remove(requestId);
            rpcFuture.done(response);
        } else {
            throw new RuntimeException("rpcFuture is null");
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }


    @Override
    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public RpcFuture sendRequest(RpcRequest request) {
        RpcFuture rpcFuture = new RpcFuture(request);
        pendingRpc.put(request.getRequestId(), rpcFuture);
        if (channel.isActive()) {
            channel.writeAndFlush(request);
        } else {
            throw new RuntimeException("channel state error");
        }

        LOGGER.debug("send request->" + request.getRequestId());
        return rpcFuture;
    }
}
