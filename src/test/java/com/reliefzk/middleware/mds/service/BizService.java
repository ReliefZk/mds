/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.reliefzk.middleware.mds.service;

/**
 *
 * @author kui.zhouk
 * @version $Id: RpcService.java, v 0.1 2018年04月06日 21:13 kui.zhouk Exp $
 */
public interface BizService {

    String say();

    String say(String word);

    String say(String to, String word);

    String say(String from, String to, String word);

}