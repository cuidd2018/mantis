package com.u002.mantis.spring;

import com.u002.mantis.Container;
import com.u002.mantis.MantisBootstrap;
import com.u002.mantis.config.api.ProviderConfig;
import com.u002.mantis.config.api.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProviderBean extends ProviderConfig implements InitializingBean, DisposableBean, ApplicationContextAware, BeanNameAware, Container {

    /**
     * slf4j logger for this class
     */
    private final Logger logger = LoggerFactory.getLogger(ProviderBean.class);

    protected transient ApplicationContext applicationContext = null;
    private transient String beanName = null;

    /**
     * @param name
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(String)
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     * <p>
     * 不支持Spring3.2以下版本, 无法通过addApplicationListener启动export
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Using implements InitializingBean
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        propertiesInit();
        //MantisBootstrap.newInstance().getProviderConfig().start();
        export();
    }

    /**
     * 组装相应的ServiceConfig
     */
    private void propertiesInit() {
        if (applicationContext != null) {
            //MantisBootstrap.newInstance().getProviderConfig().init();
            if (getServerConfigs() == null) {
                Map<String, ServerConfig> protocolMaps = applicationContext.getBeansOfType(ServerConfig.class, false, false);
                if (!CollectionUtils.isEmpty(protocolMaps)) {
                    List<ServerConfig> protocolLists = new ArrayList<>(protocolMaps.values());
                    setServerConfigs(protocolLists);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("mantis rpc destroy provider with beanName {}", beanName);
        // todo unexport
    }

    @Override
    public <T> Collection<T> findBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz, false, false).values();
    }
}
