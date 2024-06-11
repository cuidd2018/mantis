package com.u002.mantis;


/**
 * 单例的启动类
 */
public class MantisBootstrap {


    /**
     * 单例
     */
    private static final MantisBootstrap instance = new MantisBootstrap();

    public static MantisBootstrap newInstance() {
        return instance;
    }

    private Config providerConfig;

    public Container container;

    public Container getContainer() {
        return container;
    }

    public  void init() throws Exception {
        providerConfig.start();
    }

    public  void start() throws Exception {
        providerConfig.start();
    }


    public void stop(){

    }

    public Config getProviderConfig() {
        return providerConfig;
    }
}
