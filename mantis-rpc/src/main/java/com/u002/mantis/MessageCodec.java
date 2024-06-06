package com.u002.mantis;

import io.netty.channel.ChannelHandler;

/**
 * 消息编码接口
 */
public interface MessageCodec {


    ChannelHandler newEncoder();

    ChannelHandler newDecoder();
}
