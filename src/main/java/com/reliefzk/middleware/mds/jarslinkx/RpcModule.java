/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.jarslinkx;

import com.alipay.jarslink.api.ModuleConfig;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author kui.zhouk
 * @version $Id: Module.java, v 0.1 2018年04月06日 19:05 kui.zhouk Exp $
 */
public interface RpcModule {

    <T> T getService(String name, Class<T> clazz);

    <T> Map<String, T> getServices();

    String getName();

    String getVersion();

    Date getCreation();

    void destroy();

    ClassLoader getChildClassLoader();

    ModuleConfig getModuleConfig();

}