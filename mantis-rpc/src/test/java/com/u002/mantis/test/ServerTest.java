package com.u002.mantis.test;

import com.u002.mantis.provider.internal.ServiceProcessor;
import com.u002.mantis.transport.Server;
import com.u002.mantis.transport.codec.DefaultCodec;
import com.u002.mantis.transport.internal.DefaultRpcHandler;
import com.u002.mantis.transport.internal.RpcServer;
import com.u002.serialize.protostuff.ProtostuffSerialization;
import org.junit.Test;

public class ServerTest {

    @Test
    public void testServer() throws Exception {
        Server server=new RpcServer("127.0.0.1:9999",new DefaultCodec(new ProtostuffSerialization()),new DefaultRpcHandler(new ServiceProcessor()));
    }
}
