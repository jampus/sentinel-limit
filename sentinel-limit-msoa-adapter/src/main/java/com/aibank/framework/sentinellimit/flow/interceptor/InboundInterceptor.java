package com.aibank.framework.sentinellimit.flow.interceptor;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public abstract class InboundInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        BundleService bundleService = methodInvocation.getThis().getClass().getAnnotation(BundleService.class);
        intercept(bundleService, methodInvocation);
        return methodInvocation.proceed();
    }

    protected abstract Object intercept(BundleService bundleService, MethodInvocation methodInvocation);
}
