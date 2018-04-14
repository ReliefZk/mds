package com.reliefzk.middleware.mds.action;

import com.alipay.jarslink.api.Action;
import com.reliefzk.middleware.mds.service.BizService;

public class BizServiceAction implements Action<String, String> {

    private BizService bizService;

    @Override
    public String execute(String s) {
        return bizService.say();
    }

    @Override
    public String getActionName() {
        return "bizServiceAction";
    }

    public void setBizService(BizService bizService) {
        this.bizService = bizService;
    }
}
