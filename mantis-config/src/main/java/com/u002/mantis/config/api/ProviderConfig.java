package com.u002.mantis.config.api;

import com.u002.mantis.Config;
import com.u002.mantis.MantisBootstrap;
import com.u002.mantis.Remote;
import com.u002.mantis.provider.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProviderConfig extends AbstractInterfaceConfig implements Config, Remote {

    /**
     * slf4j logger for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ProviderConfig.class);


    /**
     * 接口实现类引用
     */
    protected transient Object ref;

    /**
     * 配置的协议列表
     */
    private List<ServerConfig> serverConfigs = null;

    /**
     * 是否已发布
     */
    protected volatile boolean exported = false;

    /**
     * 发布服务
     *
     * @throws Exception the init error exception
     */
    protected void export() throws Exception {
        if (!exported) {
            for (ServerConfig serverConfig : serverConfigs) {
                try {
                    serverConfig.start();
                    // 注册接口
                    Processor processor = serverConfig.getProcessor();
                    processor.registerProcessor(this);
                } catch (Exception e) {
                    logger.error("Catch exception server.", e);
                }
            }
            exported = true;
        }
    }

    /**
     * Gets ref.
     *
     * @return the ref
     */
    public Object getRef() {
        return ref;
    }

    /**
     * Sets ref.
     *
     * @param ref the ref
     */
    public void setRef(Object ref) {
        this.ref = ref;
    }

    /**
     * Gets serverConfigs.
     *
     * @return the serverConfigs
     */
    public List<ServerConfig> getServerConfigs() {
        return serverConfigs;
    }

    /**
     * Sets serverConfigs.
     *
     * @param serverConfigs the serverConfigs
     */
    public void setServerConfigs(List<ServerConfig> serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    @Override
    public void start() throws Exception {
        export();
    }

    @Override
    public void init() {
        //如果是空值，从容器工厂里边获取一次
        if (getServerConfigs() == null) {
            Collection<ServerConfig> protocolMaps = MantisBootstrap.newInstance().getContainer().findBeans(ServerConfig.class);
            if (!CollectionUtils.isEmpty(protocolMaps)) {
                List<ServerConfig> protocolLists = new ArrayList<>(protocolMaps);
                setServerConfigs(protocolLists);
            }
        }

    }
}
