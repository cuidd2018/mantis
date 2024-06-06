package com.u002.mantis.remote;

import com.u002.basic.Connection;
import com.u002.basic.client.RemoterClient;
import com.u002.basic.proxy.transport.client.NettyClient;
import com.u002.mantis.client.RpcClientHandler;
import com.u002.serialize.protostuff.ProtostuffSerialization;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectManager.class);
    private static final ConnectManager connectManager = new ConnectManager();

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    private final CopyOnWriteArrayList<Connection> connectedHandlers = new CopyOnWriteArrayList<Connection>();
    private final Map<InetSocketAddress, Connection> connectedServerNodes = new ConcurrentHashMap<InetSocketAddress, Connection>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition connected = lock.newCondition();
    protected long connectTimeoutMills = 6000;
    private final AtomicInteger roundRobin = new AtomicInteger(0);
    private volatile boolean isRunning = true;
    private final RemoterClient remoterClient;

    private ConnectManager() {
        remoterClient=new NettyClient(new ProtostuffSerialization(), new RpcClientHandler());
    }

    public static ConnectManager getInstance() {
        return connectManager;
    }

    public void updateConnectedServer(List<String> allServerAddress) {
        if (CollectionUtils.isNotEmpty(allServerAddress)) {
            //update local serverNodes cache
            HashSet<InetSocketAddress> newAllServerNodeSet = new HashSet<InetSocketAddress>();
            for (String serverAddress : allServerAddress) {
                String[] array = serverAddress.split(":");
                if (array.length == 2) { // Should check IP and port
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    final InetSocketAddress remotePeer = new InetSocketAddress(host, port);
                    newAllServerNodeSet.add(remotePeer);
                }
            }
            // Add new server node
            for (final InetSocketAddress serverNodeAddress : newAllServerNodeSet) {
                if (!connectedServerNodes.containsKey(serverNodeAddress)) {
                    connectServerNode(serverNodeAddress);
                }
            }
            // Close and remove invalid server nodes
            for (int i = 0; i < connectedHandlers.size(); ++i) {
                Connection connection = connectedHandlers.get(i);
                SocketAddress remotePeer = connection.getRemoteAddress();
                if (!newAllServerNodeSet.contains(remotePeer)) {
                    LOGGER.info("Remove invalid server node " + remotePeer);
                    Connection handler = connectedServerNodes.get(remotePeer);
                    handler.close();
                    connectedServerNodes.remove(remotePeer);
                    connectedHandlers.remove(connection);
                }
            }
        } else { // No available server node ( All server nodes are down )
            LOGGER.error("No available server node. All server nodes are down !!!");
            clearConnectedServer();
        }
    }

    public void clearConnectedServer() {
        for (final Connection connectedServerHandler : connectedHandlers) {
            SocketAddress remotePeer = connectedServerHandler.getRemoteAddress();
            Connection handler = connectedServerNodes.get(remotePeer);
            handler.close();
            connectedServerNodes.remove(connectedServerHandler);
        }
        connectedHandlers.clear();
    }

    public void reconnect(final Connection connection, final SocketAddress remotePeer) {
        if (connection != null) {
            connectedHandlers.remove(connection);
            connectedServerNodes.remove(connection.getRemoteAddress());
        }
        connectServerNode((InetSocketAddress) remotePeer);
    }

    private void connectServerNode(final InetSocketAddress remotePeer) {
        threadPoolExecutor.submit(new Runnable() {
            public void run() {

                remoterClient.connect(remotePeer, new RemoterClient.Listener() {
                    @Override
                    public void onSuccess(Connection clientHandler) {
                        addHandler(remotePeer,clientHandler);
                    }

                    @Override
                    public void onClose() {
                        clearConnectedServer();
                    }
                });

            }
        });
    }

    private void addHandler(InetSocketAddress remotePeer,Connection handler) {
        connectedHandlers.add(handler);
        connectedServerNodes.put(remotePeer, handler);
        signalAvailableHandler();
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            return connected.await(this.connectTimeoutMills, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    public void connect(final String serverAddress) {
        List<String> allServerAddress = Arrays.asList(serverAddress.split(","));
        updateConnectedServer(allServerAddress);
    }

    public Connection chooseHandler() {
        CopyOnWriteArrayList<Connection> handlers = (CopyOnWriteArrayList<Connection>) this.connectedHandlers.clone();
        int size = handlers.size();
        while (isRunning && size == 0) {
            try {
                boolean available = waitingForHandler();
                if (available) {
                    handlers = (CopyOnWriteArrayList<Connection>) this.connectedHandlers.clone();
                    size = handlers.size();
                }
            } catch (InterruptedException e) {
                LOGGER.error("Waiting for available node is interrupted! ", e);
                throw new RuntimeException("Can't connect any servers!", e);
            }
        }
        int index = (roundRobin.getAndAdd(1) + size) % size;
        return handlers.get(index);
    }

    public void stop() {
        isRunning = false;
        for (Connection connection : connectedHandlers) {
            connection.close();
        }
        signalAvailableHandler();
        threadPoolExecutor.shutdown();
        remoterClient.stop();
    }
}
