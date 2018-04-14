package com.reliefzk.middleware.mds.jarslinkx.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcAction implements InvocationHandler {

	private Object target;

	public RpcAction(Object target){
        this.target = target;
    }

	@Override
	public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
		return method.invoke(target, args);
	}

}
