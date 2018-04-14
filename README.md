# mds module deployment solustion

基于蚂蚁金服微贷事业部开源jarslink进行二次开发，主要优化点在：原生实现的action方式为静态代理，本branch实现为动态代理，
使客户端应用更直观。
jarslink: https://github.com/alibaba/jarslink

原生使用方式：
```
<bean class="com.reliefzk.middleware.mds.service.BizServiceImpl" id="bizService" />
<!-- jarslink原生实现<静态代理> -->
<bean id="bizServiceAction2" class="com.reliefzk.middleware.mds.action.BizServiceAction">
        <property name="bizService" ref="bizService"/>
</bean>
```
```
Module actionModule = moduleManager.find("demo1");
Action<String, String> action = actionModule.getAction("bizServiceAction");
String result = action.execute("");
```

优化后使用方式：
```
<bean class="com.reliefzk.middleware.mds.service.BizServiceImpl" id="bizService" />
<!-- rpc action -->
<rpcaction:action id="bizServiceAction1" ref="bizService" class="com.reliefzk.middleware.mds.service.BizService" />
```
```
BizService rpcService = rpcModuleManager.getService("demo2", "bizServiceAction", BizService.class);
String result1 = rpcService.say();
String result2 = rpcService.say("哇靠");
String result3 = rpcService.say("李白","好诗");
String result4 = rpcService.say("苏轼", "李白","好诗");

```