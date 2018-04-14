/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.service;

/**
 *
 * @author kui.zhouk
 * @version $Id: RpcServiceImpl.java, v 0.1 2018年04月06日 21:13 kui.zhouk Exp $
 */
public class BizServiceImpl implements BizService {
    @Override
    public String say() {
        return "I say to myself!";
    }

    @Override
    public String say(String word) {
        return "I say '" + word + "' to myself";
    }

    @Override
    public String say(String to, String word) {
        return "I say '" + word + "' to " + to;
    }

    @Override
    public String say(String from, String to, String word) {
        return from + " say '" + word + "' to " + to;
    }
}