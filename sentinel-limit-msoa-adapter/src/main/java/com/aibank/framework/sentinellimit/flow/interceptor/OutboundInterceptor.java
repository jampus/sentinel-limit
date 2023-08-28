package com.aibank.framework.sentinellimit.flow.interceptor;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import static com.aibank.framework.sentinellimit.flow.proxy.OutboundBeanPostProcessor.methodBundleServiceMap;

public abstract class OutboundInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        BundleService bundleService = methodBundleServiceMap.get(method);
        intercept(bundleService, methodInvocation);
        return methodInvocation.proceed();
    }

    protected abstract Object intercept(BundleService bundleService, MethodInvocation methodInvocation);
}
