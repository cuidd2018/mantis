package com.u002.mantis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class RpcFuture implements Future<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcFuture.class);

    /**
     * 同步控制器
     */
    private final Sync sync;
    private final RpcRequest request;
    private volatile RpcResponse response;
    private final long startTime;

    private static  final  long responseTimeThreshold = 5000;

    /**
     * 回调容器
     */
    private List<AsyncRPCCallback> pendingCallbacks = new ArrayList<AsyncRPCCallback>();
    private final ReentrantLock lock = new ReentrantLock();


    public RpcFuture(RpcRequest request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isDone() {
        return sync.isDone();
    }

    public Object get() throws InterruptedException, ExecutionException {
        LOGGER.debug("requestId"+request.getRequestId());
        sync.acquire(1);
        if (this.response != null) {
            return this.response.getResult();
        } else {
            return null;
        }
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.response != null) {
                return this.response.getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
                    + ". Request class name: " + this.request.getClassName()
                    + ". Request method: " + this.request.getMethodName());
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(RpcResponse response) {
        LOGGER.debug("response......");
        this.response = response;
        sync.release(1);
        invokeCallbacks();
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            LOGGER.warn("Service response time is too slow. Request id = " + response.getRequestId() + ". Response Time = " + responseTime + "ms");
        }
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            for (final AsyncRPCCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }
    }

    public RpcFuture addCallback(AsyncRPCCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    private void runCallback(final AsyncRPCCallback callback) {
        final RpcResponse response = this.response;
        FutureCallBack.execute(this.response,callback);

    }

    static class Sync extends AbstractQueuedSynchronizer {


        // future status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquires) {
            System.out.println(Thread.currentThread().getName()+"-->a+");
            return getState() == done;
        }

        protected boolean tryRelease(int releases) {
            System.out.println(Thread.currentThread().getName()+"-->b");
            if (getState() == pending) {
                return compareAndSetState(pending, done);
            }
            return false;
        }

        public boolean isDone() {
            getState();
            return getState() == done;
        }
    }

    private static interface AsyncRPCCallback<T> {
        void success(T result);

        void fail(Exception e);
    }


    private static class FutureCallBack{
        private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
        public static void execute(RpcResponse response,AsyncRPCCallback callback){
            executor.submit(new Runnable() {
                public void run() {
                    if (!response.isError()) {
                        callback.success(response.getResult());
                    } else {
                        callback.fail(new RuntimeException("Response error", new Throwable(response.getError())));
                    }
                }
            });
        }
    }
}
