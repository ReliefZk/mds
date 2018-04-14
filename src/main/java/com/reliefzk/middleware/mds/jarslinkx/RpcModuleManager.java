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
package com.reliefzk.middleware.mds.jarslinkx;

import java.util.List;
import java.util.Map;


public interface RpcModuleManager {

    /**
     * find Module with name
     *
     * @param name module name
     * @return module instance
     */
    RpcModule find(String name);

    /**
     * 根据模块名和版本查找Module
     * @param name   模块名称
     * @param version 模块版本号
     * @return
     */
    RpcModule find(String name, String version);

    /**
     * 激活模块的某个版本为默认版本
     *
     * @param name    module name
     * @param version module version
     */
    void activeVersion(String name, String version);

    /**
     * 获得模块激活的版本
     *
     * @param name
     * @return
     */
    String getActiveVersion(String name);

    /**
     * 获取所有已加载的Module
     *
     * @return
     */
    List<RpcModule> getModules();

    /**
     * 注册一个Module
     *
     * @param module 模块
     * @return 旧模块, 如果没有旧模块则返回null
     */
    RpcModule register(RpcModule module);

    /**
     * 移除已激活版本的Module
     *
     * @param name 模块名
     * @return 被移除的模块
     */
    RpcModule remove(String name);

    /**
     * 移除一个Module
     *
     * @param name 模块名
     * @param version  版本号
     * @return 被移除的模块
     */
    RpcModule remove(String name, String version);

    /**
     * 获取加载失败的模块异常信息
     *
     * @return key:模块名,value:错误信息
     */
    Map<String, String> getErrorModuleContext();

    <T> T getService(String moduleName, String service, Class serviceClass);

}
