/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.jarslinkx.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.reliefzk.middleware.mds.jarslinkx.RpcModule;
import com.reliefzk.middleware.mds.jarslinkx.RpcModuleManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;

/**
 *
 * @author kui.zhouk
 * @version $Id: RpcModuleManagerImpl.java, v 0.1 2018年04月06日 18:55 kui.zhouk Exp $
 */
public class RpcModuleManagerImpl implements RpcModuleManager, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcModuleManagerImpl.class);

    /**
     * 已注册的所有模块,key:moduleName upperCase
     */
    private final ConcurrentHashMap<String, RpcRuntimeModule> allModules = new ConcurrentHashMap();

    private RpcRuntimeModule getRuntimeModule(String name) {
        RpcRuntimeModule runtimeModule = allModules.get(name.toUpperCase());
        return runtimeModule != null ? runtimeModule : new RpcRuntimeModule();
    }

    @Override
    public List<RpcModule> getModules() {
        List<RpcModule> modules = Lists.newArrayList();
        for (String name : allModules.keySet()) {
            RpcRuntimeModule runtimeModule = getRuntimeModule((String) name);
            for (String version : runtimeModule.getModules().keySet()) {
                modules.add(runtimeModule.getModules().get(version));
            }
        }
        return ImmutableList.copyOf(filter(modules, instanceOf(RpcSpringModule.class)));
    }

    @Override
    public RpcModule find(String name) {
        checkNotNull(name, "module name is null");
        String defaultVersion = getDefaultVersion(name);
        checkNotNull(defaultVersion, "module default version is null");
        return find(name, defaultVersion);
    }

    private String getDefaultVersion(String name) {return getRuntimeModule((String) name).getDefaultVersion();}

    @Override
    public RpcModule find(String name, String version) {
        checkNotNull(name, "module name is null");
        checkNotNull(version, "module version is null");
        return getRuntimeModule((String) name).getModule(version);
    }

    @Override
    public void activeVersion(String name, String version) {
        checkNotNull(name, "module name is null");
        checkNotNull(version, "module version is null");
        getRuntimeModule((String) name).setDefaultVersion(version);
    }

    @Override
    public String getActiveVersion(String name) {
        checkNotNull(name, "module name is null");
        return getDefaultVersion(name);
    }

    @Override
    public RpcModule register(RpcModule module) {
        checkNotNull(module, "module is null");
        String name = module.getName();
        String version = module.getVersion();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("register Module: {}-{}", name, version);
        }

        //same module and same version can not register
        RpcModule registeredModule = getRuntimeModule(name).getModule(version);
        if (registeredModule != null) {
            return null;
        }

        RpcRuntimeModule runtimeModule = getRuntimeModule(name);
        RpcModule oldModule = null;
        //module frist register
        if (runtimeModule.getModules().isEmpty()) {
            runtimeModule = new RpcRuntimeModule().withName(name).withDefaultVersion(version).addModule(module);
            allModules.put(name.toUpperCase(), runtimeModule);
        } else {
            //the same module to register again
            oldModule = runtimeModule.getDefaultModule();
            runtimeModule.addModule(module).setDefaultVersion(version);
            // remove module old version
            if (oldModule != null && module.getModuleConfig().isNeedUnloadOldVersion() && !runtimeModule.getModules().isEmpty()) {
                runtimeModule.getModules().remove(oldModule.getVersion());
            }
        }

        return oldModule;
    }

    @Override
    public RpcModule remove(String name) {
        checkNotNull(name, "module name is null");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Remove Module: {}", name);
        }
        return remove(name, getRuntimeModule((String) name).getDefaultVersion());
    }

    @Override
    public RpcModule remove(String name, String version) {
        checkNotNull(name, "module name is null");
        checkNotNull(version, "module version is null");
        return getRuntimeModule((String) name).getModules().remove(version);
    }

    @Override
    public void destroy() throws Exception {
        for (RpcModule each : getModules()) {
            try {
                each.destroy();
            } catch (Exception e) {
                LOGGER.error("Failed to destroy module: " + each.getName(), e);
            }
        }
        allModules.clear();
    }

    @Override
    public Map<String, String> getErrorModuleContext() {

        Map<String, String> result = Maps.newHashMap();

        for (String name : allModules.keySet()) {
            RpcRuntimeModule runtimeModule = getRuntimeModule((String) name);
            result.put(name, runtimeModule.getErrorContext());
        }

        return result;
    }

    @Override
    public <T> T getService(String moduleName, String service, Class rpcServiceClass) {
        RpcRuntimeModule runtimeModule = allModules.get(StringUtils.upperCase(moduleName));
        return (T)runtimeModule.getDefaultModule().getService(service, rpcServiceClass);
    }

}