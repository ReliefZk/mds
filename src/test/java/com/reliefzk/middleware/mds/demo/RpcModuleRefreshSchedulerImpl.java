/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.demo;

import com.alipay.jarslink.api.ModuleConfig;
import com.google.common.collect.ImmutableList;
import com.reliefzk.middleware.mds.jarslinkx.impl.RpcAbstModuleRefreshScheduler;

import java.util.List;

/**
 *
 * @author kui.zhouk
 */
public class RpcModuleRefreshSchedulerImpl extends RpcAbstModuleRefreshScheduler {

    private DemoModuleLoader demoModuleLoader;

    @Override
    public List<ModuleConfig> queryModuleConfigs() {
        return ImmutableList.copyOf(demoModuleLoader.buildModuleConfig());
    }

    public void setDemoModuleLoader(DemoModuleLoader demoModuleLoader) {
        this.demoModuleLoader = demoModuleLoader;
    }
}