package com.reliefzk.middleware;

import static org.junit.Assert.assertTrue;

import com.alipay.jarslink.api.Action;
import com.alipay.jarslink.api.Module;
import com.alipay.jarslink.api.ModuleManager;
import com.reliefzk.middleware.mds.jarslinkx.RpcModuleManager;
import com.reliefzk.middleware.mds.service.BizService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private RpcModuleManager rpcModuleManager;

    @Test
    public void testAction(){
        Module actionModule = moduleManager.find("demo1");
        Action<String, String> action = actionModule.getAction("bizServiceAction");
        String result = action.execute("");
    }

    @Test
    public void testRpc(){
        BizService rpcService = rpcModuleManager.getService("demo2", "bizServiceAction", BizService.class);
        String result1 = rpcService.say();
        String result2 = rpcService.say("哇靠");
        String result3 = rpcService.say("李白","好诗");
        String result4 = rpcService.say("苏轼", "李白","好诗");
    }
}
