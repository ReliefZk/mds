/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.demo;

import com.alipay.jarslink.api.ModuleConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kui.zhouk
 */
public class DemoModuleLoader {

    /**
     * module 描述文件
     */
    private String moduleSchema;

    public List<ModuleConfig> buildModuleConfig() {
        List<ModuleConfig> moduleConfigList = new ArrayList<>();
        List<String> moduleConfigs = null;
        try {
            moduleConfigs = FileUtils.readLines(new File(moduleSchema), Charset.defaultCharset().name());
        } catch (IOException e) {
            throw new IllegalStateException(moduleSchema);
        }
        Preconditions.checkState(moduleConfigList == null || moduleConfigList.size() == 0);
        for(String moduleConfig : moduleConfigs){
            if(!StringUtils.startsWith(moduleConfig, "#")){
                ModuleConfig module = parseModule(moduleConfig);
                if(module != null) {
                    moduleConfigList.add(module);
                }
            }
        }
        return moduleConfigList;
    }

    private ModuleConfig parseModule(String moduleConfig) {
        String[] modules = StringUtils.split(moduleConfig, ",");
        if(modules.length != 4){
            return null;
        }

        ModuleConfig module = new ModuleConfig();
        module.setName(modules[0]);
        module.setVersion(modules[1]);
        module.setEnabled(StringUtils.equalsIgnoreCase(modules[2], "y"));
        URL demoModule = null;
        try {
            demoModule = new File(modules[3]).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(modules[3]);
        }
        module.setModuleUrl(ImmutableList.of(demoModule));
        return module;
    }

    public void setModuleSchema(String moduleSchema) {
        this.moduleSchema = moduleSchema;
    }
}