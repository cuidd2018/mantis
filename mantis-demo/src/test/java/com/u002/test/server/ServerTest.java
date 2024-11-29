package com.u002.test.server;

import com.u002.basic.Server;
import com.u002.basic.proxy.transport.RpcServer;
import com.u002.mantis.provider.internal.ServiceProcessor;
import com.u002.basic.codec.DefaultCodec;
import com.u002.mantis.transport.internal.DefaultRpcHandler;
import com.u002.serialize.protostuff.ProtostuffSerialization;
import org.junit.Test;

public class ServerTest {

    @Test
    public void testServer() throws Exception {
        Server server=new RpcServer("127.0.0.1:9999",new DefaultCodec(new ProtostuffSerialization()),new DefaultRpcHandler(new ServiceProcessor()));
    }
}
