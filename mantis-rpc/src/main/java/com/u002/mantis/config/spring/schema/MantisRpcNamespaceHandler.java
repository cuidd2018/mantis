package com.u002.mantis.config.spring.schema;

import com.u002.mantis.config.spring.ConsumerBean;
import com.u002.mantis.config.spring.ProviderBean;
import com.u002.mantis.config.spring.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Title: 继承NamespaceHandlerSupport，将xml的标签绑定到解析器
 * <p/>
 * Description: 在META-INF下增加spring.handlers和spring.schemas
 */
public class MantisRpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("provider", new MantisRpcBeanDefinitionParser(ProviderBean.class, true));
        registerBeanDefinitionParser("consumer", new MantisRpcBeanDefinitionParser(ConsumerBean.class, true));
        registerBeanDefinitionParser("server", new MantisRpcBeanDefinitionParser(ServerBean.class, true));
    }
}
