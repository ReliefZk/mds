/*
 *
 *  * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.reliefzk.middleware.mds.jarslinkx.impl;

import com.alipay.jarslink.api.ModuleConfig;
import com.reliefzk.middleware.mds.jarslinkx.RpcModule;
import com.reliefzk.middleware.mds.jarslinkx.handler.RpcBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.context.ConfigurableApplicationContext;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 集成Spring上下文的模块,从Spring上下中找Action
 */
public class RpcSpringModule implements RpcModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcSpringModule.class);

    /**  模块的配置信息 */
    private ModuleConfig moduleConfig;

    /**  模块的名称 */
    private final String name;

    /**  模块的版本 */
    private final String version;

    /**  模块启动的时间 */
    private final Date creation;

    /* 加载的rpc bean */
    private final Map<String, RpcBean> rpcBeans;

    /**  模块中的Action，Key为大写Action名称 */
    private final Map<String, Object> services;

    private final ConfigurableApplicationContext applicationContext;

    public RpcSpringModule(ClassLoader loader, ModuleConfig moduleConfig, String version, String name, ConfigurableApplicationContext applicationContext) {
        this.moduleConfig = moduleConfig;
        this.applicationContext = applicationContext;
        this.version = version;
        this.name = name;
        this.creation = new Date();
        this.rpcBeans = scanRpcBean();
        this.services = scanServices(loader);
    }

    private Map<String, RpcBean> scanRpcBean() {
        Map<String, RpcBean> rpcBeanMap = new ConcurrentHashMap<>();

        Map<String, RpcBean> actionMap = applicationContext.getBeansOfType(RpcBean.class);
        for(RpcBean bean : actionMap.values()){
            bean.setTarget(applicationContext.getBean(bean.getRef()));
            rpcBeanMap.put(bean.getService(), bean);
        }
        return rpcBeanMap;
    }

    private List<String> initObj() {
        List<String> list = new ArrayList<>();
        for(Method method : Object.class.getClass().getMethods()) {
            list.add(method.getName());
        }
        return list;
    }

    private Map<String, Object> scanServices(ClassLoader loader)  {
        Map<String, Object> services = new ConcurrentHashMap<>();

        for(RpcBean bean : rpcBeans.values()){
            Object rpcBean = applicationContext.getBean(bean.getRef());
            RpcAction invokeHandler = new RpcAction(bean.getTarget());
            services.put(bean.getService(), Proxy.newProxyInstance(loader, rpcBean.getClass().getInterfaces(), invokeHandler));
        }
        return services;
    }

    @Override
    public <T> T getService(String name, Class<T> clazz) {
        if(StringUtils.isNotEmpty(name) && services.containsKey(name)){
            Object bean = (T)services.get(name);
            return clazz.cast(bean);
        }
        return null;
    }

    @Override
    public <T> Map<String, T> getServices() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Date getCreation() {
        return creation;
    }

    @Override
    public void destroy() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Close application context: {}", applicationContext);
        }
        if (!services.isEmpty()) {
            services.clear();
        }
        //close spring context
        closeQuietly(applicationContext);
        //clean classloader
        clear(applicationContext.getClassLoader());
    }

    /**
     * 清除类加载器
     *
     * @param classLoader
     */
    public static void clear(ClassLoader classLoader) {
        checkNotNull(classLoader, "classLoader is null");
        Introspector.flushCaches();
        //从已经使用给定类加载器加载的缓存中移除所有资源包
        ResourceBundle.clearCache(classLoader);
        //Clear the introspection cache for the given ClassLoader
        CachedIntrospectionResults.clearClassLoader(classLoader);
        LogFactory.release(classLoader);
    }

    /**
     * 关闭Spring上下文
     * @param applicationContext
     */
    private static void closeQuietly(ConfigurableApplicationContext applicationContext) {
        checkNotNull(applicationContext, "applicationContext is null");
        try {
            applicationContext.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close application context", e);
        }
    }

    @Override
    public ModuleConfig getModuleConfig() {
        return moduleConfig;
    }

    @Override
    public ClassLoader getChildClassLoader() {
        return this.applicationContext.getClassLoader();
    }

}
