/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.demo;

import com.alipay.jarslink.api.ModuleConfig;
import com.alipay.jarslink.api.impl.AbstractModuleRefreshScheduler;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 *
 * @author kui.zhouk
 */
public class ModuleRefreshSchedulerImpl extends AbstractModuleRefreshScheduler {

    private DemoModuleLoader demoModuleLoader;

    @Override
    public List<ModuleConfig> queryModuleConfigs() {
        return ImmutableList.copyOf(demoModuleLoader.buildModuleConfig());
    }

    public void setDemoModuleLoader(DemoModuleLoader demoModuleLoader) {
        this.demoModuleLoader = demoModuleLoader;
    }
}