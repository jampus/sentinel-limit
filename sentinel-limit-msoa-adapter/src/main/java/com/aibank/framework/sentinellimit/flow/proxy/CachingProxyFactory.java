package com.aibank.framework.sentinellimit.flow.proxy;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;

import java.util.*;

public class CachingProxyFactory {
    private static Map<String, Object> proxyCache = new HashMap<>();

    public static synchronized Object getProxy(Object target, Class<?> interfaceType, Advisor interceptor,BundleService bundleService) {
        String key = String.format("%s/%s/%s", interfaceType, bundleService.provider(), bundleService.version());
        Object cachedProxy = proxyCache.get(key);
        if (cachedProxy != null) {
            return cachedProxy;
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(interfaceType);
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisors(interceptor);

        Object proxy = proxyFactory.getProxy();
        proxyCache.put(key, proxy);
        return proxy;
    }
}
