package com.u002.mantis.test.client;

import com.u002.mantis.Client;
import com.u002.mantis.proxy.IAsyncObjectProxy;
import com.u002.mantis.task.RpcFuture;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-client.xml")
public class HelloServiceTest {

    @Autowired
    private Client rpcClient = null;

    @Test
    public void helloClient0() {
        System.out.println(rpcClient);
        Assert.assertNotNull(rpcClient);
    }


    @Test
    public void helloTest1() {
        HelloService helloService = rpcClient.create(HelloService.class);
        String result = helloService.hello("World");
        System.out.println(result);
        Assert.assertEquals("Hello! World", result);
    }

    @Test
    public void helloTest2() {
        HelloService helloService = rpcClient.create(HelloService.class);
        Person person = new Person("Yong", "Huang");
        String result = helloService.hello(person);
        System.out.println(result);
        Assert.assertEquals("Hello! Yong Huang", result);
    }

    @Test
    public void helloFutureTest1() throws ExecutionException, InterruptedException {
        IAsyncObjectProxy helloService = rpcClient.createAsync(HelloService.class);
        RpcFuture result = helloService.call("hello", "World");
        Assert.assertEquals("Hello! World", result.get());
    }

    @Test
    public void helloFutureTest2() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            IAsyncObjectProxy helloService = rpcClient.createAsync(HelloService.class);
            Person person = new Person("Yong", "Huang");
            RpcFuture result = helloService.call("hello", person);
            Assert.assertEquals("Hello! Yong Huang", result.get());
        }

    }

    @Test
    public void helloFutureTest3() throws ExecutionException, InterruptedException {
        IAsyncObjectProxy helloService = rpcClient.createAsync(HelloService.class);
        RpcFuture result = helloService.call("hello", "World");
        Assert.assertEquals("Hello! World", result.get());


        Person person = new Person("Yong", "Huang");
        result = helloService.call("hello", person);
        Assert.assertEquals("Hello! Yong Huang", result.get());


    }


//    @After
//    public void setTear() {
//        System.out.println("我关闭了！！");
//        if (rpcClient != null) {
//            rpcClient.stop();
//        }
//    }
}
