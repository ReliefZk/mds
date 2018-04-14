/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.jarslinkx.impl;

import com.alipay.jarslink.api.ToStringObject;
import com.reliefzk.middleware.mds.jarslinkx.RpcModule;

import java.util.concurrent.ConcurrentHashMap;

public class RpcRuntimeModule extends ToStringObject {

    private String name;

    private String defaultVersion;

    /**
     * load module error msg
     */
    private String errorContext;

    /**
     * all version module,key:version
     */
    private ConcurrentHashMap<String, RpcModule> modules = new ConcurrentHashMap();

    public RpcModule getModule(String version) {
        return modules.get(version);
    }

    public RpcModule getDefaultModule() {
        return modules.get(getDefaultVersion());
    }

    public RpcRuntimeModule addModule(RpcModule module) {
        modules.put(module.getVersion(), module);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RpcRuntimeModule withName(String name) {
        this.name = name;
        return this;
    }

    public String getDefaultVersion() {
        return defaultVersion;
    }

    public void setDefaultVersion(String defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    public RpcRuntimeModule withDefaultVersion(String defaultVersion) {
        setDefaultVersion(defaultVersion);
        return this;
    }

    public ConcurrentHashMap<String, RpcModule> getModules() {
        return modules;
    }

    public String getErrorContext() {
        return errorContext;
    }

    public void setErrorContext(String errorContext) {
        this.errorContext = errorContext;
    }

}