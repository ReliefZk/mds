/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.jarslinkx.handler;

import java.io.Serializable;

/**
 *
 * @author kui.zhouk
 * @version $Id: RpcBean.java, v 0.1 2018年04月06日 20:29 kui.zhouk Exp $
 */
public class RpcBean implements Serializable {

    private String service;
    private String ref;
    private String clazz;
    private Object target;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}