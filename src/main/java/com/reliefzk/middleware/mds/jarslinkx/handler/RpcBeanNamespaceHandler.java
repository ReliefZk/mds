/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.jarslinkx.handler;


import com.reliefzk.middleware.mds.jarslinkx.handler.RpcBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

public class RpcBeanNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("action", new RpcBeanDefinitionParser());
    }

    public static class RpcBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

        protected Class getBeanClass(Element element) {
            return RpcBean.class;
        }

        protected void doParse(Element element, BeanDefinitionBuilder bean) {
            String beanName = element.getAttribute("id");
            bean.addPropertyValue("service", beanName);
            String refBean = element.getAttribute("ref");
            bean.addPropertyValue("ref", refBean);
            String clazz = element.getAttribute("class");
            bean.addPropertyValue("clazz", clazz);
        }

    }

}